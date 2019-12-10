package ru.bitc.totdesigner.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_content_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.catalog.CatalogFragment
import ru.bitc.totdesigner.catalog.CatalogViewModel
import ru.bitc.totdesigner.catalog.state.CatalogState
import ru.bitc.totdesigner.home.state.HomeState
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.ContentListAdapter
import ru.bitc.totdesigner.platfom.adapter.QuestAdapterDelegate
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.system.dpToPx
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe

class HomeFragment : BaseFragment(R.layout.fragment_content_list) {

    private val viewModel by viewModel<HomeViewModel>()

    private val adapter by lazy {
        ListDelegationAdapter(QuestAdapterDelegate().createDelegate())
    }

    private val decorator = GridPaddingItemDecoration(3, 24.dpToPx(),false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateState()
        rvCardQuest.addItemDecoration(decorator)
        rvCardQuest.adapter = adapter
        subscribe(viewModel.viewState, ::handleState)
    }

    private fun handleState(homeState: HomeState) {
        adapter.setData(homeState.questItems)
        tvDescription.text = homeState.description
        tvTitle.text = homeState.title
        tvListTitle.text = homeState.listTitle
    }

    companion object {
        fun newInstance() = with(HomeFragment()) {
            this
        }
    }
//    //test
//    val quests: ArrayList<TestQuestDTO> = ArrayList()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater!!.inflate(R.layout.fragment_content_list,container,false)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val recycler = view.findViewById<RecyclerView>(R.id.rv_content_view)
//        recycler.layoutManager
//
//        //test
//        quests.add(
//            TestQuestDTO(
//                "Оригами 1",
//                R.drawable.flamingo_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Оригами 2",
//                R.drawable.fox_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Оригами 3",
//                R.drawable.kolibri_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Оригами 4",
//                R.drawable.fox_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Оригами 5",
//                R.drawable.kolibri_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Оригами 6",
//                R.drawable.flamingo_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Анатомия 1",
//                R.drawable.anatomy_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Анатомия 2",
//                R.drawable.anatomy_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Анатомия 3",
//                R.drawable.anatomy_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Астрономия 1",
//                R.drawable.star_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Астрономия 2",
//                R.drawable.star_quest
//            )
//        )
//        quests.add(
//            TestQuestDTO(
//                "Астрономия 3",
//                R.drawable.star_quest
//            )
//        )
//
//        var qAdapter =
//            ContentListAdapter(quests, this)
//        recycler.adapter = qAdapter
//
//        var gridLayoutManager = GridLayoutManager(activity,3)
//        recycler.layoutManager = gridLayoutManager
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//    }
//
//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//    }
}

data class TestQuestDTO(
    var name: String,
    var imgResId: Int
)
