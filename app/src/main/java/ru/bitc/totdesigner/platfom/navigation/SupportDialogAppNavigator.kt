package ru.bitc.totdesigner.platfom.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.*
import java.util.*
import kotlin.contracts.ExperimentalContracts


/**
 * Created on 22.01.2020
 * @author YWeber */

open class SupportDialogAppNavigator(
    private val activity: FragmentActivity,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) :
    Navigator {

    private var localStackCopy: LinkedList<String>? = null

    constructor(activity: FragmentActivity, containerId: Int) : this(
        activity,
        activity.supportFragmentManager,
        containerId
    )

    override fun applyCommands(commands: Array<Command>) {
        fragmentManager.executePendingTransactions()

        //copy stack before apply commands
        copyStackToLocal()

        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun copyStackToLocal() {
        localStackCopy = LinkedList()

        val stackSize = fragmentManager.backStackEntryCount
        for (i in 0 until stackSize) {
            localStackCopy?.add(fragmentManager.getBackStackEntryAt(i).name!!)
        }
    }

    /**
     * Perform transition described by the navigation command
     *
     * @param command the navigation command to apply
     */
    private fun applyCommand(command: Command) {
        when (command) {
            is Forward -> activityForward(command)
            is Replace -> activityReplace(command)
            is BackTo -> backTo(command)
            is Back -> fragmentBack()
        }
    }

    private fun activityForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)

        // Start activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
        } else {
            fragmentForward(command)
        }
    }

    private fun fragmentForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val fragment = createFragment(screen)

        if (fragment is DialogFragment) {
            forwardDialogFragment(fragment, command, screen)
        } else forwardFragmentBase(fragment, command, screen)
    }

    private fun forwardDialogFragment(
        fragment: DialogFragment,
        command: Forward,
        screen: SupportAppScreen
    ) {
        fragment.show(fragmentManager, screen.screenKey)
        localStackCopy?.add(screen.screenKey)
    }

    private fun forwardFragmentBase(
        fragment: Fragment?,
        command: Forward,
        screen: SupportAppScreen
    ) {

        val fragmentTransaction = fragmentManager.beginTransaction()

        setupFragmentTransaction(
            command,
            fragmentManager.findFragmentById(containerId),
            fragment,
            fragmentTransaction
        )

        fragment?.let {
            fragmentTransaction
                .replace(containerId, it)
                .addToBackStack(screen.screenKey)
                .commit()
            localStackCopy?.add(screen.screenKey)
        }

    }

    private fun fragmentBack() {
        localStackCopy?.let {
            if (it.size > 0) {
                fragmentManager.popBackStack()
                it.removeLast()
            } else {
                activityBack()
            }
        }

    }

    private fun activityBack() {
        activity.finish()
    }

    private fun activityReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)

        // Replace activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
            activity.finish()
        } else {
            fragmentReplace(command)
        }
    }

    private fun fragmentReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val fragment = createFragment(screen)

        localStackCopy?.let {
            if (it.size > 0) {
                fragmentManager.popBackStack()
                it.removeLast()

                val fragmentTransaction = fragmentManager.beginTransaction()

                setupFragmentTransaction(
                    command,
                    fragmentManager.findFragmentById(containerId),
                    fragment,
                    fragmentTransaction
                )

                fragment?.let { frag ->
                    fragmentTransaction
                        .replace(containerId, frag)
                        .addToBackStack(screen.screenKey)
                        .commit()
                    it.add(screen.screenKey)
                }

            } else {
                val fragmentTransaction = fragmentManager.beginTransaction()

                setupFragmentTransaction(
                    command,
                    fragmentManager.findFragmentById(containerId),
                    fragment,
                    fragmentTransaction
                )

                fragment?.let { frag ->
                    fragmentTransaction
                        .replace(containerId, frag)
                        .commit()
                }

            }
        }

    }

    /**
     * Performs [BackTo] command transition
     */
    private fun backTo(command: BackTo) {
        if (command.screen == null) {
            backToRoot()
        } else {
            val key = command.screen.screenKey
            val index = localStackCopy?.indexOf(key) ?: -1
            val size = localStackCopy?.size ?: 0

            if (index != -1) {
                for (i in 1 until size - index) {
                    localStackCopy?.removeLast()
                }
                fragmentManager.popBackStack(key, 0)
            } else {
                backToUnexisting(command.screen as SupportAppScreen)
            }
        }
    }

    private fun backToRoot() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        localStackCopy?.clear()
    }

    /**
     * Override this method to setup fragment transaction [FragmentTransaction].
     * For example: setCustomAnimations(...), addSharedElement(...) or setReorderingAllowed(...)
     *
     * @param command             current navigation command. Will be only [Forward] or [Replace]
     * @param currentFragment     current fragment in container
     * (for [Replace] command it will be screen previous in new chain, NOT replaced screen)
     * @param nextFragment        next screen fragment
     * @param fragmentTransaction fragment transaction
     */
    private fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction
    ) {
    }

    /**
     * Override this method to create option for start activity
     *
     * @param command        current navigation command. Will be only [Forward] or [Replace]
     * @param activityIntent activity intent
     * @return transition options
     */
    private fun createStartActivityOptions(command: Command, activityIntent: Intent): Bundle? {
        return null
    }

    private fun checkAndStartActivity(
        screen: SupportAppScreen,
        activityIntent: Intent,
        options: Bundle?
    ) {
        // Check if we can start activity
        if (activityIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(activityIntent, options)
        } else {
            unexistingActivity(screen, activityIntent)
        }
    }

    /**
     * Called when there is no activity to open `screenKey`.
     *
     * @param screen         screen
     * @param activityIntent intent passed to start Activity for the `screenKey`
     */
    private fun unexistingActivity(screen: SupportAppScreen, activityIntent: Intent) {
        // Do nothing by default
    }

    /**
     * Creates Fragment matching `screenKey`.
     *
     * @param screen screen
     * @return instantiated fragment for the passed screen
     */
    private fun createFragment(screen: SupportAppScreen): Fragment? {
        val fragment = screen.fragment

        if (fragment == null) {
            errorWhileCreatingScreen(screen)
        }
        return fragment
    }

    /**
     * Called when we tried to fragmentBack to some specific screen (via [BackTo] command),
     * but didn't found it.
     *
     * @param screen screen
     */
    private fun backToUnexisting(screen: SupportAppScreen) {
        backToRoot()
    }

    private fun errorWhileCreatingScreen(screen: SupportAppScreen) {
        throw RuntimeException("Can't create a screen: " + screen.screenKey)
    }
}
