package com.example.mc_prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mc_prototype.ui.theme.MCPrototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MCPrototypeTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation(
    recipeViewModel: RecipeViewModel = viewModel(),
) {
    val navController = rememberNavController()
    val recipes by recipeViewModel.recipes.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToAddRecipe = { navController.navigate("add_recipe") },
                onNavigateToMyRecipes = { navController.navigate("my_recipes") },
            )
        }
        composable("add_recipe") {
            AddRecipeScreen(
                onBack = { navController.popBackStack() },
                onRecipeSaved = { newRecipe ->
                    recipeViewModel.addRecipe(newRecipe)
                    navController.navigate("my_recipes") {
                        popUpTo("add_recipe") { inclusive = true }
                    }
                },
            )
        }
        composable("my_recipes") {
            MyRecipesScreen(
                recipes = recipes,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
