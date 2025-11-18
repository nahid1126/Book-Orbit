package com.nahid.book_orbit.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class UseCaseQualifier {
    LOGIN_REQUEST,SIGN_UP_REQUEST
}

enum class InputFieldSize{
    SMALL, MEDIUM, LARGE
}

enum class InputType{
    TEXT, ONLY_NUMBER, DECIMAL_NUMBER, PASSWORD
}

enum class QuestionType{
    TEXT, RADIO, DROPDOWN, CHECKBOX
}
enum class FarmerInfoType{
    NATIONAL, AREA, ZONE, SUBAREA, VILLAGE
}

enum class FarmerType{
    FARMER,TAKA,AREA_ITEM,ITEM,;
    companion object{
        fun default() = FARMER
    }
}
enum class NavigationDestinations(
    val value: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
) {
    HOME("Home", Icons.Outlined.Home, Icons.Filled.Home),
    Books("Books", Icons.Outlined.AutoStories, Icons.Filled.AutoStories),
    PROFILE("Profile", Icons.Outlined.Person, Icons.Filled.Person)
}