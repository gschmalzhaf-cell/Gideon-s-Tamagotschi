package com.gideon.tamagotchi.ui.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gideon.tamagotchi.R
import com.gideon.tamagotchi.domain.model.PetType
import kotlinx.coroutines.launch

private val petColors = listOf(
    0xFFD6336CL,
    0xFFF08C00L,
    0xFFF4D03FL,
    0xFF2F9E44L,
    0xFF3B82F6L,
    0xFF845EC2L
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePetScreen(
    onPetCreated: () -> Unit,
    viewModel: CreatePetViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PetType.BLOB) }
    var selectedColor by remember { mutableStateOf(petColors.first()) }
    var isSaving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.create_title)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = stringResource(id = R.string.create_name_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(id = R.string.create_type_label),
                style = MaterialTheme.typography.titleMedium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PetType.entries.forEach { type ->
                    FilterChip(
                        selected = type == selectedType,
                        onClick = { selectedType = type },
                        label = { Text(text = stringResource(id = type.labelRes)) }
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.create_color_label),
                style = MaterialTheme.typography.titleMedium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                petColors.forEach { hex ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(hex))
                            .then(
                                if (hex == selectedColor) {
                                    Modifier.border(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shape = CircleShape
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { selectedColor = hex }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        isSaving = true
                        viewModel.createPet(name, selectedType, selectedColor)
                        isSaving = false
                        onPetCreated()
                    }
                },
                enabled = name.isNotBlank() && !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.create_submit))
            }
        }
    }
}
