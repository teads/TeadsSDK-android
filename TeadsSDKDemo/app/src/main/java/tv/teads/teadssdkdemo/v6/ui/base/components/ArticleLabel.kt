package tv.teads.teadssdkdemo.v6.ui.base.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tv.teads.teadssdkdemo.R

@Composable
fun ArticleLabel(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.article)
) {
    Row(
        modifier = Modifier.padding(start = 12.dp, top = 18.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.2.sp,
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                )
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun ArticleLabelPreview() {
    MaterialTheme {
        ArticleLabel(
            modifier = Modifier.padding(16.dp)
        )
    }
}
