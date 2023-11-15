package com.example.tummoctask.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.tummoctask.model.local.CartItem
import com.example.tummoctask.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel, navController: NavHostController) {

    val cartItems by cartViewModel.allCartItems.collectAsState(emptyList())
    val totalPrice by cartViewModel.totalPrice.collectAsState(0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart") },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent, titleContentColor = Color.Black, navigationIconContentColor = Color.White),
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4D67F5),
                            Color(0xFFCBCFE7)
                        )
                    )
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (cartItems.isEmpty()) {
                Text(text = "Your cart is empty.")
            } else {
                LazyColumn {
                    items(cartItems) { item ->
                        CartItemRow(item = item, cartViewModel)
                    }
                    item {
                        Button(
                            onClick = {
                                navController.popBackStack()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 20.dp)
                        ) {
                            Text(text = "Checkout with total price: $$totalPrice")
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun CartItemRow(item: CartItem, cartViewModel: CartViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 30.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.icon)
                    .crossfade(true)
                    .build(),
                contentDescription = item.itemName,
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary, modifier = Modifier.scale(0.5f)
                    )
                },
                success = {
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = item.itemName, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Text(text = item.quantity.toString(), textAlign = TextAlign.Center)
            Text(text = "$${item.price}", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = { cartViewModel.deleteCartItem(item) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .width(150.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(text = "Remove item")
            }
        }
    }
}
