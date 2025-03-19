package com.example.grouppay.ui.features.groups.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.grouppay.R
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.groups.model.GroupWithTotalExpense


@Composable
fun GroupItemV2(group: GroupWithTotalExpense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // Adjust the height as needed
        ) {
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {

                Image(
                    painter = painterResource(R.mipmap.icons8_split_money_1),
                    contentDescription = "Group Image",
                    alpha = 0.5F,
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Black, Color.Transparent),
                                startX = 0F,
                                endX = 200F
                            )
                        )
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startX = 0F,
                                endX = 20F
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterStart) // Align the content to the center start
            ) {
                CommonText(
                    text = group.groupName,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_add_group),
                        contentDescription = "Members Icon"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    CommonText(
                        text = "Members: ${group.totalMembers} Members",
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_settlement),
                        contentDescription = "Amount Spent Icon"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    CommonText(
                        text = "Total amount spent: ₹${group.totalAmountSpent}",
                    )
                }
            }
        }
    }


}