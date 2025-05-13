package com.example.movienest_2_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.Room
import com.example.movienest_2_1.Databse.AppDatabase
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.movienest_2_1.Databse.Movie
import com.example.movienest_2_1.Databse.MovieData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject



// Video Demonstration Video Link::
// https://drive.google.com/file/d/1UoYl4Yj2ZavvnL0FLN-xHHE95IBeYWuO/view?usp=sharing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "movieNestDB")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            db.movieDao().deleteAll()
        }



        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = Color.Black
                    ) {

                        bottomNavItems.forEach{bottomNavItem ->


                            NavigationBarItem(
                                selected = currentRoute == bottomNavItem.route,

                                onClick = {
                                    if (currentRoute != bottomNavItem.route) {
                                        navController.navigate(bottomNavItem.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (bottomNavItem.badges !=0) {
                                                Badge {
                                                    Text(text = bottomNavItem.badges.toString())
                                                }
                                            }
                                            else if (bottomNavItem.hasNews){
                                                Badge()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (currentRoute == bottomNavItem.route) bottomNavItem.selectedItem else bottomNavItem.unselectedItem,
                                            contentDescription =  bottomNavItem.title,

                                            )
                                    }
                                },
                                label = {
                                    Text(
                                        text = bottomNavItem.title,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    }
                },
                floatingActionButton = {}
            ) {
                val padding = it
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(padding)
                ) {
                    composable("home") { HomeScreen(db= db, navController) }
                    composable("search") { SearchScreen(db= db) }
                    composable("movies") { MovieScreen(db= db) }
                    composable("titleSearch") { TitleSearchScreen(db=db)  }
                }
            }
        }
    }
}



@Composable
fun HomeScreen(db: AppDatabase, navController: NavController) {
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var dialogMessage by rememberSaveable { mutableStateOf("") }

    val imageList = listOf(
        R.drawable.slide1,
        R.drawable.slide2,
        R.drawable.slide3,
        R.drawable.slide4
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Database Status", color = Color.White) },
            text = { Text(dialogMessage, color = Color.LightGray) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("OK", color = Color.White) }
            },
            containerColor = Color.DarkGray,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isPortrait) {
            /** ========== 📱 PORTRAIT UI ========== **/
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo2),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(120.dp)
                        .padding(16.dp),
                    contentScale = ContentScale.FillWidth
                )

                Text(
                    text = "Recent Released Movies",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, bottom = 10.dp)
                )
                Spacer(modifier = Modifier.padding(20.dp))

                // Movie slider
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(36.dp),
                    contentPadding = PaddingValues(horizontal = 15.dp)
                ) {
                    items(imageList) { imageRes ->
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .aspectRatio(15 / 9f)
                                .height(250.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .border(3.dp, Color.White, RoundedCornerShape(15.dp))
                        )
                    }
                }

                // Dots
                Row(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .height(20.dp)
                        .size(50.dp)
                        .background(Color.Gray, RoundedCornerShape(15.dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    imageList.forEach { _ ->
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .padding(horizontal = 1.dp)
                                .background(Color.White, shape = CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(40.dp))

                ButtonsSection(db, scope, navController, dialogMessage) {
                    dialogMessage = it
                    showDialog = true
                }
            }

        } else {
            /** ========== 🖥️ LANDSCAPE UI ========== **/
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ✅ Centered logo at the top
                Image(
                    painter = painterResource(id = R.drawable.logo2),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(100.dp)
                        .padding(12.dp),
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    // 🎞 Movie List on the Left
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(imageList) { imageRes ->
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                            )
                        }
                    }

                    // 🎛 Buttons on the Right
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ButtonsSection(db, scope, navController, dialogMessage) {
                            dialogMessage = it
                            showDialog = true
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ButtonsSection(
    db: AppDatabase,
    scope: CoroutineScope,
    navController: NavController,
    dialogMessage: String,
    onDialogMessageChange: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedButton(
                onClick = {
                    scope.launch {
                        try {
                            db.movieDao().insertAll(MovieData.movies)
                            onDialogMessageChange("Movies added successfully!")
                        } catch (e: Exception) {
                            onDialogMessageChange("Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text("Add Movies DB", color = Color.Black)
            }

            ElevatedButton(
                onClick = {
                    navController.navigate("search") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text("Search Movies", color = Color.Black)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedButton(
                onClick = {
                    navController.navigate("movies") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text("Search Actors", color = Color.Black)
            }

            ElevatedButton(
                onClick = { navController.navigate("titleSearch") {
                    popUpTo("home") { inclusive = true }
                } },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text("Search Title", color = Color.Black)
            }
        }
    }
}


 suspend fun fetchMovieDetails(title: String, nextId:Int): Movie? {
    val apiKey = "c7656958"
    val urlString = "https://www.omdbapi.com/?t=${title}&apikey=${apiKey}"

    val url = URL(urlString)
    val con = url.openConnection() as HttpURLConnection

     val response = StringBuilder()

     return withContext(Dispatchers.IO) {
         try {
             val reader = con.inputStream.bufferedReader()
             var line: String? = reader.readLine()
             while (line != null) {
                 response.append(line)
                 line = reader.readLine()
             }
             parseMovieToObject(response.toString(), nextId)
         } catch (e: Exception) {
             null
         }
     }
}

fun parseMovieToObject(jsonString: String,nextId: Int): Movie {
    val json = JSONObject(jsonString)



    if (json.getString("Response") == "False") {
        throw IllegalArgumentException("Movie not found")
    }

    return Movie(
        id = nextId, // Room will auto-generate if setup
        title = json.getString("Title"),
        year = json.getString("Year"),
        rated = json.getString("Rated"),
        released = json.getString("Released"),
        runtime = json.getString("Runtime"),
        genre = json.getString("Genre"),
        director = json.getString("Director"),
        writer = json.getString("Writer"),
        actors = json.getString("Actors"),
        plot = json.getString("Plot")
    )
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(db: AppDatabase){
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    var movieID by remember { mutableStateOf(5) }
    val scope = rememberCoroutineScope()
    var movieInfo by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT





    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Database Status", color = Color.White)
               },
            text = { Text(dialogMessage, color = Color.LightGray) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK", color = Color.White)
                }
            },
            containerColor = Color.DarkGray,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .padding(16.dp),
                contentScale = ContentScale.FillWidth
            )



            OutlinedTextField(
                value = searchText,
                onValueChange = { text ->
                    searchText = text

                },
                label = { Text("Search Movie") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                )
            )


            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        val lastId = db.movieDao().getLastMovieId() ?: 0
                        val nextId = lastId + 1
                        movieInfo = fetchMovieDetails(searchText, nextId)
                        isLoading = false
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.height(16.dp))


            Spacer(modifier = Modifier.height(26.dp))
            // Movie List

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(36.dp))
            } else {
                movieInfo?.let {movieData->
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(20.dp)) // 3D shadow
                            .background(Color.DarkGray), // Background behind shadow
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)) // Slightly lighter than black
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = movieData.title,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text("🎭 Actors: ${movieData.actors}", color = Color.White)
                            Text("📅 Year: ${movieData.year}", color = Color.White)
                            Text("🎬 Genre: ${movieData.genre}", color = Color.White)
                            Text("🔖 Rated: ${movieData.rated}", color = Color.White)
                            Text("🗓️ Released: ${movieData.released}", color = Color.White)
                            Text("⏱️ Runtime: ${movieData.runtime}", color = Color.White)
                            Text("🎥 Director: ${movieData.director}", color = Color.White)
                            Text("✍️ Writer: ${movieData.writer}", color = Color.White)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("📖 Plot:\n${movieData.plot}", color = Color.White)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        scope.launch {

                                            try {


                                                db.movieDao().insertMovie(movieData)

                                                dialogMessage = "Movie saved successfully!"
                                                showDialog = true
                                                movieInfo = null // Clear movie details
                                                searchText = ""
                                                active = false


                                            }catch (e:Exception){
                                                dialogMessage = "Error saving movie: ${e.message}"
                                                showDialog = true
                                            }

                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White, // Teal accent
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Save")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Button(
                                    onClick = {
                                        movieInfo = null // Clear movie details
                                        searchText = ""      // Also clear search input
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Close")
                                }
                            }
                        }
                    }
                }
            }


            val genres = listOf(
                "Action", "Adventure", "Crime",
                "Comedy", "Documentary", "Drama",
                "Fantasy", "Horror", "Historical",
                "Thriller", "Sci - Fi", "More >"
            )

            val actors = listOf(
                "Tom Hanks", "Scarlett", "Brad Pitt",
                "Meryl Streep", "Robert Jr.", "Emma Stone",
                "Leonardo D", "Jennifer L", "Johnny Depp",
                "Chris Evans", "Natalie Portman", "Chris H"
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item{
                    Text(
                        text = "Genres",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3), // 3 items per row
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        items(genres) { genre ->
                            Button(
                                onClick = { /* Handle click */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.DarkGray,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(text = genre, fontSize = 14.sp, maxLines = 1)
                            }
                        }
                    }
                }

                item{
                    Text(
                        text = "Actors",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3), // 3 items per row
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        items(actors) { actor ->
                            Button(
                                onClick = { /* Handle click */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.DarkGray,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(text = actor, fontSize = 14.sp, maxLines = 1)
                            }
                        }
                    }
                }




            }


        }
    }







}


@Composable
fun MovieScreen(db: AppDatabase) {
    var searchText by remember { mutableStateOf("") }
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        movies = db.movieDao().getAllMovies()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .padding(16.dp),
                contentScale = ContentScale.FillWidth
            )



            OutlinedTextField(
                value = searchText,
                onValueChange = { text ->
                    searchText = text
                    scope.launch {
                        movies = if (text.isBlank()) {
                            db.movieDao().getAllMovies()
                        } else {
                            db.movieDao().searchMoviesByActor(text)
                        }
                    }
                },
                label = { Text("Search by Actor") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                )


            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "All The Movies...",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            Spacer(modifier = Modifier.height(26.dp))
            // Movie List
            var selectedMovie by remember { mutableStateOf<Movie?>(null) }
            LazyColumn {
                items(movies) { movie ->
                    MovieCardCompact(movie) {
                        selectedMovie = it
                    }
                }
            }

            selectedMovie?.let { movie ->
                MovieDetailDialog(movie = movie, onDismiss = { selectedMovie = null })
            }
        }
    }

}

@Composable
fun MovieCardCompact(movie: Movie, onCardClick: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onCardClick(movie) }, // 👈 Make it clickable
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "🎭 ${movie.actors.take(30)}...",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )

            Text(
                text = "📅 ${movie.year} | 🎬 ${movie.genre}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun MovieDetailDialog(movie: Movie, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text("🎭 Actors: ${movie.actors}", color = Color.White)
                Text("📅 Year: ${movie.year}", color = Color.White)
                Text("🎬 Genre: ${movie.genre}", color = Color.White)
                Text("🔖 Rated: ${movie.rated}", color = Color.White)
                Text("🗓️ Released: ${movie.released}", color = Color.White)
                Text("⏱️ Runtime: ${movie.runtime}", color = Color.White)
                Text("🎥 Director: ${movie.director}", color = Color.White)
                Text("✍️ Writer: ${movie.writer}", color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Text("📖 Plot:\n${movie.plot}", color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Close", color = Color.Black)
                }
            }
        }
    }
}




@Composable
fun TitleSearchScreen(db: AppDatabase){
    var searchText by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    var apiMovies by remember { mutableStateOf(listOf<Movie>()) }
    var isLoading by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .padding(16.dp),
                contentScale = ContentScale.FillWidth
            )



            OutlinedTextField(
                value = searchText,
                onValueChange = { text ->
                    searchText = text
                    scope.launch {
                        isLoading = true
                        val lastId = db.movieDao().getLastMovieId() ?: 0
                        val nextId = lastId + 1
                        apiMovies = fetchMoviesByTitlePartial(searchText,nextId)
                        isLoading = false
                    }
                },
                label = { Text("Search by Title") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                )


            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "All The Movies In Web...",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            Spacer(modifier = Modifier.height(26.dp))
            // Movie List
            var selectedMovie by remember { mutableStateOf<Movie?>(null) }
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(36.dp))
            } else {
                LazyColumn {
                    items(apiMovies) { movie ->
                        MovieCardCompact(movie) {

                        }
                    }
                }
            }


        }
    }
}

suspend fun fetchMoviesByTitlePartial(query: String,nextId: Int): List<Movie> {
    val apiKey = "c7656958"
    val urlString = "https://www.omdbapi.com/?s=${query}&apikey=${apiKey}"
    val url = URL(urlString)
    val con = url.openConnection() as HttpURLConnection

    return withContext(Dispatchers.IO) {
        try {
            val response = StringBuilder()
            val reader = con.inputStream.bufferedReader()
            var line: String? = reader.readLine()
            while (line != null) {
                response.append(line)
                line = reader.readLine()
            }

            parseMovieList(response.toString(),nextId)
        } catch (e: Exception) {
            emptyList()
        } finally {
            con.disconnect()
        }
    }
}
fun parseMovieList(jsonString: String,nextId: Int): List<Movie> {
    val json = JSONObject(jsonString)
    if (json.optString("Response") == "False") {
        return emptyList()
    }

    val moviesJsonArray = json.getJSONArray("Search")
    val movieList = mutableListOf<Movie>()

    for (i in 0 until moviesJsonArray.length()) {
        val movieJson = moviesJsonArray.getJSONObject(i)
        movieList.add(
            Movie(
                id = nextId+1,
                title = movieJson.getString("Title"),
                year = movieJson.getString("Year"),
                rated = "",
                released = "",
                runtime ="",
                genre = "",
                director = "",
                writer = "",
                actors = "",
                plot = ""
            )
        )
    }

    return movieList
}



val  bottomNavItems = listOf(
    BottomNavItem(
        title = "Home",
        route = "home",
        selectedItem = Icons.Filled.Home,
        unselectedItem = Icons.Outlined.Home,
        hasNews = false,
        badges = 0
    ),

    BottomNavItem(
        title = "Search",
        route = "search",
        selectedItem = Icons.Filled.Search,
        unselectedItem = Icons.Outlined.Search,
        hasNews = false,
        badges = 0
    ),

    BottomNavItem(
        title = "Movies",
        route = "movies",
        selectedItem = Icons.Filled.Favorite,
        unselectedItem = Icons.Outlined.Favorite,
        hasNews = false,
        badges = 0
    ),
    BottomNavItem(
        title = "Search Title",
        route = "titleSearch",
        selectedItem = Icons.Filled.Search,
        unselectedItem = Icons.Outlined.Search,
        hasNews = false,
        badges = 0
    )




)


data class BottomNavItem(
    val title: String,
    val route : String,
    val selectedItem : ImageVector,
    val unselectedItem : ImageVector,
    val hasNews: Boolean,
    val badges : Int
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}