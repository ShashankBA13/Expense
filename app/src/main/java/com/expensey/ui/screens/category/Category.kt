package com.expensey.ui.screens.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expensey.data.models.Category
import com.expensey.ui.theme.ExpenseyTheme


@Composable
fun CategoryScreen(onAddCategory: (String) -> Unit) {
	var isDialogVisible by remember { mutableStateOf(false) }
	var newCategoryText by remember { mutableStateOf("") }

	val viewModel: CategoryViewModel = viewModel()

	val categoryList by viewModel.categoryLiveDataList.observeAsState()

	Column(
		modifier = Modifier.fillMaxSize()
	) {

		Row (
			modifier = Modifier.padding(0.dp, 20.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				imageVector = Icons.Outlined.ArrowBackIos,
				contentDescription = "Go Back", // Provide a content description as needed
				modifier = Modifier.clickable {
					// Handle the icon click action
				} then Modifier
					.padding(20.dp, 0.dp)
					.size(25.dp)
			)
			Text(
				text = "Category",
				modifier = Modifier.weight(1f),
				style = TextStyle(
					fontSize = 20.sp,
					fontWeight = FontWeight.Bold
				)
			)
		}

		LazyColumn {
			items(categoryList?: emptyList()) { category : Category ->
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(20.dp, 15.dp),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = category.categoryName,
						modifier = Modifier.weight(1f) // Expand the Text to take available space
					)
					// Add your icon here
					Icon(
						imageVector = Icons.Outlined.Edit,
						contentDescription = "Edit Category", // Provide a content description as needed
						modifier = Modifier.clickable {
							// Handle the icon click action
						} then Modifier.padding(20.dp, 0.dp)
					)
					Icon(
						imageVector = Icons.Outlined.Delete,
						contentDescription = "Delete Category", // Provide a content description as needed
						modifier = Modifier.clickable {
							// Handle the icon click action
						},
						tint = Color.Red
					)
				}
			}
		}

		FloatingActionButton(
			onClick = { isDialogVisible = true  },
			modifier = Modifier
				.padding(20.dp)
				.align(Alignment.End)
		) {
			Icon(Icons.Filled.Add, "Floating action button.")
		}

		if (isDialogVisible) {
			DialogWithInput(
				onDismissRequest = { isDialogVisible = false },
				onConfirmation = {
					// Handle the confirmation action, e.g., save the new category
					if (newCategoryText.isNotBlank()) {
						onAddCategory(newCategoryText)
					}
					isDialogVisible = false
					newCategoryText = ""
				},
				viewModel
			)
		}
	}
}

@Preview
@Composable
fun CategoryPreview() {
	ExpenseyTheme {
		CategoryScreen() {}
	}
}

@Composable
fun DialogWithInput(
	onDismissRequest: () -> Unit,
	onConfirmation: () -> Unit,
	viewModel: CategoryViewModel
) {
	var text by rememberSaveable { mutableStateOf("") }

	Dialog(onDismissRequest = { onDismissRequest() }) {
		// Draw a rectangle shape with rounded corners inside the dialog
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(150.dp),
			shape = RoundedCornerShape(16.dp),
		) {
			Column(
				modifier = Modifier
					.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally,
			) {

				Column {
					TextField(
						value = text,
						onValueChange = { text = it },
						label = { Text("Category") },
						placeholder = { Text("Food") }
					)
				}

				Row(
					modifier = Modifier
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center,
				) {
					TextButton(
						onClick = { onDismissRequest() },
						modifier = Modifier
							.padding(8.dp)
							.width(100.dp),
					) {
						Text("Cancel")
					}
					TextButton(
						onClick = {
							viewModel.addCategory(Category(categoryName = text))
							onConfirmation()
					    },
						modifier = Modifier
							.padding(8.dp)
							.width(100.dp),
					) {
						Text("Save")
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun PreviewDialog() {
	var showDialog by remember { mutableStateOf(false) }

	// Get an instance of your view model
	val viewModel: CategoryViewModel = viewModel()

	ExpenseyTheme {
		DialogWithInput(
			onDismissRequest = { showDialog = false },
			onConfirmation = { showDialog = false },
			viewModel = viewModel // Pass the view model
		)
	}
}