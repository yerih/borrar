package com.mivuelto.core.ui

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mivuelto.core.ui.navigation.NavFeature


enum class NavItem {

}

sealed class NavCommand(
    internal val feature: NavFeature,
    internal val subRoute: String = "",
    private val subRoutes: List<String> = emptyList(),
    private val navArgs: List<NavArgs> = emptyList(),
){
    class Type(feature: NavFeature) : NavCommand(feature)
    class TypeSubRoute(
        feature: NavFeature,
        subRoute: String = "",
        subRoutes: List<String> = emptyList(),
        args: List<NavArgs> = emptyList(),
    ) : NavCommand(feature, subRoute, subRoutes, args){
        fun createRoute(args: NavArgs) = "${feature.route}/$subRoute/${args.key}"
    }

    val route = run {
        val out = mutableListOf<String>()
        if(feature.route.isNotEmpty()) out.add(feature.route)
        if(subRoute.isNotEmpty()) out.add(subRoute)
        if(subRoutes.isNotEmpty()) out.addAll(subRoutes)
        if(navArgs.isNotEmpty()) out.addAll(navArgs.map{ "{${it.key}}" })
        out.joinToString ("/" )
    }
    val dest = run {
        val out = mutableListOf<String>()
        if(feature.route.isNotEmpty()) out.add(feature.route)
        if(subRoute.isNotEmpty()) out.add(subRoute)
        if(subRoutes.isNotEmpty()) out.addAll(subRoutes)
        out.joinToString ( "/" ).plus("/")
    }

    val args = navArgs.map { navArgument(it.key){ type = it.type } }

}

enum class NavArgs(val key: String, val type: NavType<*>){ //val navType: NavType<*>){
    ItemId("itemId", NavType.IntType),
    IsSuccess("isSuccess", NavType.BoolType),
    Code("code", NavType.IntType),
    Message("msg", NavType.StringType)
}

