package org.classup.bitmanipulationgadget;

import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.res.vectorResource

public class NavItemInfo (val label: String = "", val icon: ImageVector = Icons.Filled.Warning, val route: String = "") {
    @Composable
    fun getAllNavItems(): List<NavItemInfo> {
        return listOf(
            NavItemInfo("AND", ImageVector.vectorResource(R.drawable.and), DestinationScreens.And.route),
            NavItemInfo("OR", ImageVector.vectorResource(R.drawable.or), DestinationScreens.Or.route),
            NavItemInfo("XOR", ImageVector.vectorResource(R.drawable.xor), DestinationScreens.Xor.route),
            NavItemInfo("COMP", ImageVector.vectorResource(R.drawable.complement), DestinationScreens.Complement.route),
            NavItemInfo("SHIFT", ImageVector.vectorResource(R.drawable.shift), DestinationScreens.Shift.route)
        )
    }
}
