package ru.bitc.totdesigner.model.models

import com.tickaroo.tikxml.annotation.*

/**
 * Created on 12.03.2020
 * @author YWeber */

@Xml(name = "ConstructorStage")
data class Stage(
    @Path("WorkItems") @Element(name = "WorkItem") val workItem: List<WorkItem>?,
    @PropertyElement(name = "Name") val name: String,
    @PropertyElement(name = "Description") val description: String,
    @PropertyElement(name = "Preview") val preview: String,
    @PropertyElement(name = "Position") val position: Int,
    @Element(name = "GridSettings") val gridSetting: GridSetting,
    @Path("Assets") @Element(name = "guid") val assetsGuid: List<StageAsset>?

)

@Xml(name = "WorkItem")
data class WorkItem(
    @PropertyElement(name = "ConstantGuid") val guid: String,
    @Element(name = "Transformation") val transformation: Transformation,
    @PropertyElement(name = "ZOrder") val zOrder: Int,
    @PropertyElement(name = "IsStatic") val isStatic: Boolean,
    @PropertyElement(name = "IsClickable") val isClickable: Boolean,
    @PropertyElement(name = "isCheckable") val isCheckable: Boolean,
    @PropertyElement(name = "CheckZOrder") val checkZOrder: Boolean,
    @PropertyElement(name = "Error") val error: Int
)

@Xml(name = "GridSettings")
data class GridSetting(
    @Path("WorkSheetSize") @PropertyElement(name = "Width") val workSheetSizeWidth: Int,
    @Path("WorkSheetSize") @PropertyElement(name = "Height") val workSheetSizeHeight: Int,
    @Path("GridCellSize") @PropertyElement(name = "Width") val gridCellSizeWidth: Int,
    @Path("GridCellSize") @PropertyElement(name = "Height") val gridCellSizeHeight: Int
)

@Xml
data class StageAsset(@TextContent val guid: String)