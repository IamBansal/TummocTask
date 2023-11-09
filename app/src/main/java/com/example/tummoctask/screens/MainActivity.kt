package com.example.tummoctask.screens

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.tummoctask.model.local.CartItem
import com.example.tummoctask.model.local.FavoriteItem
import com.example.tummoctask.model.remote.Category
import com.example.tummoctask.model.remote.Item
import com.example.tummoctask.viewmodel.CartViewModel
import com.example.tummoctask.viewmodel.CategoryViewModel
import com.example.tummoctask.viewmodel.FavViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val jsonList = jsonToList(applicationContext)
            val cartViewModel: CartViewModel = viewModel()
            val favoriteViewModel: FavViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "main"
            ) {
                composable("main") {
                    MainScreen(navController, jsonList, cartViewModel, favoriteViewModel)
                }
                composable("cart") {
                    CartScreen(cartViewModel, navController)
                }
                composable("favorites") {
                    FavoritesScreen(favoriteViewModel)
                }
            }
        }
    }

    private fun jsonToList(context: Context): Item {
        val gson = Gson()
        val itemType = object : TypeToken<Item>() {}.type

        try {
            val inputStream = context.assets.open("shopping.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }
            return gson.fromJson(jsonString, itemType)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Item(emptyList(), "", "empty", true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    jsonList: Item,
    cartViewModel: CartViewModel,
    favoriteViewModel: FavViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping App") },
                actions = {
                    ToolbarButtons(navController, cartViewModel)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent, titleContentColor = Color.Black, navigationIconContentColor = Color.White),
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE08C10),
                            Color(0xFFEEC853)
                        )
                    )
                )
            )
        },
    ) { innerPadding ->
        val categoryViewModel = CategoryViewModel()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(jsonList.categories) {
                ItemContent(it, cartViewModel, favoriteViewModel, categoryViewModel)
            }
        }
    }
}

@Composable
fun ItemContent(
    category: Category,
    cartViewModel: CartViewModel,
    favoriteViewModel: FavViewModel,
    categoryViewModel: CategoryViewModel
) {

    val isExpanded = categoryViewModel.expandedStates[category.name] ?: false

    val icon: ImageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                val newExpandedState = !isExpanded
                categoryViewModel.expandedStates[category.name] = newExpandedState }
        ) {
            Text(text = category.name, fontWeight = FontWeight.Bold)
            Icon(imageVector = icon, contentDescription = null)
        }

        if (isExpanded) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    items(category.items) { item ->
                        val isFav by favoriteViewModel.isPresent(item.name).collectAsState(false)
                        val isAdded by cartViewModel.isPresent(item.name).collectAsState(false)
                        CardWithNetworkImage(
                            image = item.icon,
                            title = item.name,
                            price = item.price,
                            isFavorite = isFav,
                            isAdded = isAdded,
                            onFavoriteClick = {
                                if (isFav) favoriteViewModel.deleteFavItemByName(item.name)
                                else favoriteViewModel.insertFavItem(
                                    FavoriteItem(
                                        itemName = item.name,
                                        icon = item.icon,
                                        price = item.price
                                    )
                                )
                            },
                            onAddClick = {
                                cartViewModel.insertCartItem(
                                    CartItem(
                                        itemName = item.name,
                                        icon = item.icon,
                                        price = item.price,
                                        quantity = 1
                                    )
                                )
                                Toast.makeText(
                                    context,
                                    "${item.name} is added to the cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            )
        }

    }
}

@Composable
fun CardWithNetworkImage(
    image: String,
    title: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onAddClick: () -> Unit,
    price: Double,
    isAdded: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
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

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$$price",
                style = MaterialTheme.typography.bodySmall,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onFavoriteClick() }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Add to cart",
                    tint = if (isAdded) Color.Blue else Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onAddClick() }
                )
            }

        }
    }
}

@Composable
fun ToolbarButtons(navController: NavHostController, cartViewModel: CartViewModel) {

    ToolbarWithCartIcon(cartViewModel, navController)

    IconButton(
        onClick = { navController.navigate("favorites") }
    ) {
        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites", tint = Color.Black)
    }
}

@Composable
fun ToolbarWithCartIcon(cartViewModel: CartViewModel, navController: NavHostController) {

    val cartItemCount by cartViewModel.itemCount.collectAsState(0)

    Box(contentAlignment = Alignment.TopEnd) {
        IconButton(onClick = { navController.navigate("cart") }) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.Black)
        }

        Text(
            text = cartItemCount.toString(),
//            text = "1",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .background(Color.Red)
                .padding(4.dp)
        )
    }

//    LaunchedEffect(cartItemCount) {}
}

