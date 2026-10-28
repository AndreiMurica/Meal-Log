package com.example.mc_prototype

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.util.UUID

data class IngredientItemState(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val caloriesPer100g: String = "",
    val proteinPer100g: String = "",
    val amountGrams: String = "",
    val nameError: String? = null,
    val caloriesError: String? = null,
    val proteinError: String? = null,
    val amountError: String? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    onBack: () -> Unit,
    onRecipeSaved: (Recipe) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf<String?>(null) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }

    var ingredients by remember {
        mutableStateOf(listOf(IngredientItemState()))
    }
    var ingredientsGeneralError by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            imageError = null
        }
    }

    fun validateAndSave() {
        var isValid = true

        // 1. Title validation
        if (title.isBlank()) {
            titleError = "Recipe title cannot be blank"
            isValid = false
        } else {
            titleError = null
        }

        // 2. Photo validation
        if (selectedImageUri == null) {
            imageError = "Please select a photo of the ingredients"
            isValid = false
        } else {
            imageError = null
        }

        // 3. Ingredients validation
        if (ingredients.isEmpty()) {
            ingredientsGeneralError = "At least one ingredient is required"
            isValid = false
        } else {
            ingredientsGeneralError = null
        }

        val updatedIngredients = ingredients.map { item ->
            var itemValid = true

            val nameErr = if (item.name.isBlank()) {
                itemValid = false
                "Ingredient name is required"
            } else null

            val calVal = item.caloriesPer100g.toDoubleOrNull()
            val calErr = if (item.caloriesPer100g.isBlank()) {
                itemValid = false
                "Calories required"
            } else if ((calVal == null) || (calVal < 0.0)) {
                itemValid = false
                "Must be a valid number ≥ 0"
            } else null

            val protVal = item.proteinPer100g.toDoubleOrNull()
            val protErr = if (item.proteinPer100g.isBlank()) {
                itemValid = false
                "Protein required"
            } else if ((protVal == null) || (protVal < 0.0)) {
                itemValid = false
                "Must be a valid number ≥ 0"
            } else null

            val amtVal = item.amountGrams.toDoubleOrNull()
            val amtErr = if (item.amountGrams.isBlank()) {
                itemValid = false
                "Amount required"
            } else if ((amtVal == null) || (amtVal <= 0.0)) {
                itemValid = false
                "Must be a valid amount > 0"
            } else null

            if (!itemValid) {
                isValid = false
            }

            item.copy(
                nameError = nameErr,
                caloriesError = calErr,
                proteinError = protErr,
                amountError = amtErr,
            )
        }

        ingredients = updatedIngredients

        if (isValid) {
            val recipe = Recipe(
                title = title.trim(),
                photoUri = selectedImageUri.toString(),
                ingredients = updatedIngredients.map { item ->
                    Ingredient(
                        name = item.name.trim(),
                        caloriesPer100g = item.caloriesPer100g.toDouble(),
                        proteinPer100g = item.proteinPer100g.toDouble(),
                        amountGrams = item.amountGrams.toDouble(),
                    )
                },
            )
            onRecipeSaved(recipe)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Section 1: Recipe Title
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if ((titleError != null) && it.isNotBlank()) {
                            titleError = null
                        }
                    },
                    label = { Text("Recipe title") },
                    isError = titleError != null,
                    supportingText = {
                        if (titleError != null) {
                            Text(
                                text = titleError!!,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            // Section 2: Photo Selection
            Column {
                Text(
                    text = "Recipe Photo",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (selectedImageUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                                )
                            }
                    ) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected photo preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Replace photo")
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Select photo")
                        }
                    }
                }

                if (imageError != null) {
                    Text(
                        text = imageError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                    )
                }
            }

            // Section 3: Ingredients List
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                if (ingredientsGeneralError != null) {
                    Text(
                        text = ingredientsGeneralError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                ingredients.forEachIndexed { index, ingredientItem ->
                    IngredientCard(
                        index = index,
                        item = ingredientItem,
                        onItemChange = { updated ->
                            ingredients = ingredients.toMutableList().apply {
                                set(index, updated)
                            }
                        },
                        onRemove = {
                            ingredients = ingredients.toMutableList().apply {
                                removeAt(index)
                            }
                        },
                        canRemove = ingredients.size > 1,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedButton(
                    onClick = {
                        ingredients += IngredientItemState()
                        ingredientsGeneralError = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add ingredient")
                }
            }

            // Section 4: Save Button
            Button(
                onClick = { validateAndSave() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Text(
                    text = "Save Recipe",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
fun IngredientCard(
    index: Int,
    item: IngredientItemState,
    onItemChange: (IngredientItemState) -> Unit,
    onRemove: () -> Unit,
    canRemove: Boolean,
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Ingredient #${index + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (canRemove) {
                    IconButton(onClick = onRemove) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove ingredient",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            OutlinedTextField(
                value = item.name,
                onValueChange = {
                    onItemChange(item.copy(name = it, nameError = null))
                },
                label = { Text("Ingredient name") },
                isError = item.nameError != null,
                supportingText = item.nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = item.caloriesPer100g,
                onValueChange = {
                    onItemChange(item.copy(caloriesPer100g = it, caloriesError = null))
                },
                label = { Text("Calories per 100 g (kcal)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = item.caloriesError != null,
                supportingText = item.caloriesError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = item.proteinPer100g,
                onValueChange = {
                    onItemChange(item.copy(proteinPer100g = it, proteinError = null))
                },
                label = { Text("Protein per 100 g (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = item.proteinError != null,
                supportingText = item.proteinError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = item.amountGrams,
                onValueChange = {
                    onItemChange(item.copy(amountGrams = it, amountError = null))
                },
                label = { Text("Amount used in recipe (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = item.amountError != null,
                supportingText = item.amountError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        }
    }
}
