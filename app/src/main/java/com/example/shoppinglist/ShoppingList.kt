package com.example.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.Purple80
import com.example.shoppinglist.ui.theme.backBrush
import com.example.shoppinglist.ui.theme.lightcyan

data class shoppingItem(
    val id:Int,
    var name:String,
    var quantity:Int,
    var price:Double,
    var isEditing:Boolean = false)

fun totalPrice(itemList:List<shoppingItem>): Double{
    var total = 0.0
    for (item in itemList){
        total = total + (item.price * item.quantity)
    }
    return total

}


@Composable //Composable that is displayed when clicked on edit icon
fun shoppingItemEditor(item:shoppingItem, onEditComplete: (String, Int, Double) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var editedPrice by remember { mutableStateOf(item.price.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {

        Column {
        BasicTextField(
            value = editedName,
            onValueChange = { editedName = it },
            singleLine = true,
            modifier = Modifier.wrapContentSize().padding(8.dp)
        )

        BasicTextField(
            value = editedQuantity,
            onValueChange = { editedQuantity = it },
            singleLine = true,
            modifier = Modifier.wrapContentSize().padding(8.dp)
        )

        BasicTextField(
            value = editedPrice,
            onValueChange = { editedPrice = it },
            singleLine = true,
            modifier = Modifier.wrapContentSize().padding(8.dp)
        )

        }

        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuantity.toIntOrNull()?: 1, editedPrice.toDouble())
        }) { Text("Save") }
    }
}

@Composable
fun ShoppingListUi() {
    var showDialog by remember { mutableStateOf(false) }
    var sItems by remember { mutableStateOf(listOf<shoppingItem>()) }
    var sName by remember { mutableStateOf("") }
    var sQuantity by remember { mutableStateOf("") }
    var sPrice by remember { mutableStateOf("") }

Box(modifier = Modifier.background(backBrush)) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(36.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.wrapContentSize().padding(16.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(lightcyan, contentColor = Color.Black)
            ) {
                Text(text = "Add")

            }

            Text(
                text = "Total Price: ${totalPrice(sItems)}",
                color = Color.Black,
                modifier = Modifier.padding(16.dp))
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        )
        {
            items(sItems) { item ->
                if (item.isEditing == true) {
                    shoppingItemEditor(item = item, onEditComplete = { a, b, c ->
                        sItems = sItems.map { it.copy(isEditing = false) }
                        val editedItem = sItems.find { it.id == item.id }
                        editedItem?.let {
                            it.name = a
                            it.quantity = b
                            it.price = c
                            //a is editedName, b is editedQuantity, c is editedPrice which passed in the above composable on line 93
                        }
                    })
                } else {
                    shoppingList(
                        item = item,
                        onEditClick = {
                            sItems = sItems.map { it.copy(isEditing = (it.id == item.id)) }
                        },
                        onDeleteClick = { sItems = sItems - item }
                    )
                }
            }
        }
    }
}

    if(showDialog==true){
        AlertDialog(
            onDismissRequest = {showDialog=false},
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        if(sName.isNotBlank()) {
                            val newItem = shoppingItem(id=sItems.size+1, name=sName, quantity = sQuantity.toInt(), price = sPrice.toDouble()) //instance of data class created stored using a
                            sItems = sItems + newItem                                                              // MutableState of list of shoppingItem 'sItems'
                            showDialog = false
                            sName = ""
                            sQuantity= ""
                            sPrice = ""
                        }
                    }
                    ) { Text("Add") }

                    Button(onClick = {
                        showDialog=false
                        sName = ""
                        sQuantity= ""
                        sPrice = ""
                    }
                    ) { Text("Cancel") }
                }
            },
            title = { Text("Add shopping item")},
            text = {
                Column() {
                    OutlinedTextField(
                        value = sName,
                        onValueChange = {sName=it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Item name")}
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = sQuantity,
                        onValueChange = {sQuantity=it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Quantity")},
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = sPrice,
                        onValueChange = {sPrice=it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Price per Unit")},
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }
        )
    }
}

@Composable //non-edit state, items are displayed like this
fun shoppingList(
    item:shoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () ->Unit
)
{
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(border = BorderStroke(2.dp, color = Purple80), shape = RoundedCornerShape(20)),
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text=item.name, Modifier.align(Alignment.CenterVertically).padding(8.dp), color = Color.Black)

        Text(text="Qty: ${item.quantity}", Modifier.align(Alignment.CenterVertically), color = Color.Black)

        Text(text = "Price: ${item.quantity*item.price}", Modifier.align(Alignment.CenterVertically), color = Color.Black)

        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Black)) { //Edit Icon
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)

            }

            IconButton(onClick = onDeleteClick, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Black)) { //Delete Icon
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)

            }
        }
    }
}