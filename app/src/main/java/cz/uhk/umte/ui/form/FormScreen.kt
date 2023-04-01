package cz.uhk.umte.ui.form

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.uhk.umte.R
import cz.uhk.umte.data.enums.Gender

@Preview
@Composable
fun FormScreen() {

    val context = LocalContext.current

    var inputName by remember { mutableStateOf("") }
    var inputLastName by remember { mutableStateOf("") }
    val genderSelection = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.form_screen_appbar_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // ((Activity) context) java
                        /*
                            if (context instanceof Activity) {
                                ((Activity) context).finish()
                            }
                         */
                        (context as? Activity)?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                elevation = 12.dp,
            )
        }
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.width(48.dp),
                )
                Text(
                    text = stringResource(R.string.form_screen_title),
                    style = MaterialTheme.typography.h4,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputName,
                onValueChange = { inputName = it },
                label = {
                    Text(stringResource(R.string.form_screen_input_name))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputLastName,
                onValueChange = { inputLastName = it },
                label = {
                    Text(stringResource(R.string.form_screen_input_last_name))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                )
            )

            Spacer(Modifier.height(16.dp))

            Row {
               SingleRadioButton(label = stringResource(Gender.Man.nameRes), gender = genderSelection)
               SingleRadioButton(label = stringResource(Gender.Woman.nameRes), gender = genderSelection)
               SingleRadioButton(label = stringResource(Gender.Undefined.nameRes), gender = genderSelection)
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = { (context as Activity).finish() }) {
                Text(text = stringResource(R.string.form_screen_btn_save))
            }
        }

        it
    }
}

@Composable
fun SingleRadioButton(
    label: String,
    gender: MutableState<String>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth(),
    ) {
        RadioButton(
            selected = gender.value == label,
            onClick = { gender.value = label },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.secondary,
            ),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            modifier = Modifier
            .clickable { gender.value = label },
        )
    }
}