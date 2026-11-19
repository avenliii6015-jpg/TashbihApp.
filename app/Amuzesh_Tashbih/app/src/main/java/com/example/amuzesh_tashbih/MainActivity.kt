package com.example.amuzesh_tashbih

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF172B4D)
private val Blue = Color(0xFF3478F6)
private val Pale = Color(0xFFF3F6FC)
private val Green = Color(0xFF16845B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("student_progress", MODE_PRIVATE)
        setContent {
            var score by remember { mutableIntStateOf(prefs.getInt("score", 0)) }
            var answered by remember { mutableIntStateOf(prefs.getInt("answered", 0)) }
            MaterialTheme(colorScheme = lightColorScheme(primary = Blue, background = Pale)) {
                AppScreen(
                    score = score,
                    answered = answered,
                    onCorrect = {
                        score += 1
                        prefs.edit().putInt("score", score).apply()
                    },
                    onAnswered = {
                        answered += 1
                        prefs.edit().putInt("answered", answered).apply()
                    }
                )
            }
        }
    }
}

private data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: Int,
    val explanation: String
)

private val quiz = listOf(
    QuizQuestion(
        "در جملهٔ «علی مانند شیر شجاع است»، مشبّه کدام است؟",
        listOf("شیر", "علی", "شجاعت", "مانند"), 1,
        "علی چیزی است که به شیر مانند شده است؛ پس مشبّه است."
    ),
    QuizQuestion(
        "در «چهره‌اش چون ماه می‌درخشید»، ادات تشبیه چیست؟",
        listOf("چهره", "ماه", "چون", "می‌درخشید"), 2,
        "واژهٔ «چون» نشانه و ادات تشبیه است."
    ),
    QuizQuestion(
        "در «دانش مانند چراغ راه زندگی است»، مشبّه‌به کدام است؟",
        listOf("دانش", "چراغ", "زندگی", "راه"), 1,
        "چراغ چیزی است که دانش به آن مانند شده است."
    ),
    QuizQuestion(
        "وجه شبه در تشبیه به چه معناست؟",
        listOf("واژهٔ همانندی", "چیزی که تشبیه می‌شود", "ویژگی مشترک دو طرف", "طرف دوم تشبیه"), 2,
        "وجه شبه ویژگی مشترکی است که مشبّه و مشبّه‌به در آن همانند هستند."
    )
)

private val lessons = listOf(
    "تشبیه چیست؟" to "تشبیه یعنی همانند کردن چیزی به چیز دیگر به دلیل داشتن یک یا چند ویژگی مشترک. برای نمونه، وقتی می‌گوییم «صورتش مانند ماه است»، صورت را به ماه مانند کرده‌ایم.",
    "ارکان تشبیه" to "۱. مشبّه: چیزی که آن را به چیز دیگری مانند می‌کنیم.\n۲. مشبّه‌به: چیزی که مشبّه به آن مانند می‌شود.\n۳. ادات تشبیه: واژه‌هایی مانند «مثل»، «مانند»، «چون» و «همچون».\n۴. وجه شبه: ویژگی مشترک میان مشبّه و مشبّه‌به.",
    "انواع تشبیه" to "تشبیه از نظر حضور یا حذف ارکان شکل‌های گوناگونی دارد. در تشبیه مرسل، ادات تشبیه ذکر می‌شود و در تشبیه مؤکد، ادات حذف می‌شود. در تشبیه مفصل، وجه شبه بیان می‌شود و در تشبیه مجمل، وجه شبه ذکر نمی‌شود.",
    "روش پیدا کردن ارکان" to "ابتدا دو چیزی را که به هم مانند شده‌اند پیدا کن. سپس واژهٔ همانندی را مشخص کن و در پایان ویژگی مشترک آن دو را بیاب. همیشه به معنای جمله توجه کن؛ ممکن است ادات یا وجه شبه آشکار نباشد.",
    "تشبیه بلیغ" to "اگر ادات تشبیه و وجه شبه هر دو حذف شوند، تشبیه بلیغ پدید می‌آید؛ مانند «علم، نور است». در این مثال علم به نور مانند شده است."
)

private val examples = listOf(
    "چهره‌اش چون ماه روشن بود." to "مشبّه: چهره | مشبّه‌به: ماه | ادات: چون | وجه شبه: روشنی",
    "دل او مانند دریا بزرگ بود." to "مشبّه: دل او | مشبّه‌به: دریا | ادات: مانند | وجه شبه: بزرگی و گستردگی",
    "دانش چراغ زندگی است." to "مشبّه: دانش | مشبّه‌به: چراغ | ادات: حذف شده | وجه شبه: راهنمایی و روشنی‌بخشی",
    "قدش همچون سرو بلند بود." to "مشبّه: قد | مشبّه‌به: سرو | ادات: همچون | وجه شبه: بلندی"
)

private data class GameTask(
    val question: String,
    val options: List<String>,
    val answer: Int,
    val reason: String
)

private val gameTasks = listOf(
    GameTask("در «علی مانند شیر شجاع است»، مشبّه کدام است؟",
        listOf("علی", "شیر", "شجاعت"), 0, "علی به شیر مانند شده است."),
    GameTask("در «چهره چون ماه است»، مشبّه‌به کدام است؟",
        listOf("چهره", "چون", "ماه"), 2, "ماه طرفی است که چهره به آن مانند شده است."),
    GameTask("در «قدش مثل سرو بلند است»، ادات تشبیه چیست؟",
        listOf("قد", "مثل", "بلند"), 1, "«مثل» واژهٔ همانندی و ادات تشبیه است."),
    GameTask("در «دلش دریایی است»، وجه شبه مناسب چیست؟",
        listOf("بزرگی و گستردگی", "رنگ آبی", "سرعت"), 0, "دریا در این جمله نماد بزرگی و گستردگی است.")
)

@Composable
private fun AppScreen(score: Int, answered: Int, onCorrect: () -> Unit, onAnswered: () -> Unit) {
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = Pale,
        topBar = {
            Column(Modifier.fillMaxWidth().background(Navy).padding(horizontal = 20.dp, vertical = 18.dp)) {
                Text("آموزش تشبیه", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)
                Text("یادگیری آرایهٔ ادبی با مثال و تمرین", color = Color(0xFFDCE7FA),
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right, fontSize = 13.sp)
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val labels = listOf("خانه", "آموزش", "مثال‌ها", "آزمون", "بازی")
                val icons = listOf(Icons.Default.Home, Icons.Default.MenuBook, Icons.Default.AutoStories,
                    Icons.Default.Quiz, Icons.Default.Extension)
                labels.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = tab == index,
                        onClick = { tab = index },
                        icon = { Icon(icons[index], contentDescription = label) },
                        label = { Text(label, fontSize = 10.sp) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (tab) {
                0 -> HomePage(score, answered, { tab = 1 }, { tab = 3 })
                1 -> LessonsPage()
                2 -> ExamplesPage()
                3 -> QuizPage(onCorrect, onAnswered)
                4 -> GamePage(onCorrect, onAnswered)
            }
        }
    }
}

@Composable
private fun HomePage(score: Int, answered: Int, goLearn: () -> Unit, goQuiz: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Blue), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.End) {
                    Text("به دنیای آرایه‌های ادبی خوش آمدی!", color = Color.White,
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
                    Spacer(Modifier.height(8.dp))
                    Text("با درس‌های کوتاه، نمونه‌ها و بازی، تشبیه را بهتر یاد بگیر.",
                        color = Color.White, textAlign = TextAlign.Right)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = goLearn, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                        Text("شروع یادگیری", color = Blue)
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("امتیاز", score.toString(), Modifier.weight(1f))
                StatCard("پاسخ‌ها", answered.toString(), Modifier.weight(1f))
            }
        }
        item { SectionTitle("بخش‌های برنامه") }
        item { FeatureCard("آموزش کامل", "تعریف، ارکان و انواع تشبیه", Icons.Default.MenuBook, goLearn) }
        item { FeatureCard("آزمون چهارگزینه‌ای", "دانسته‌هایت را بسنج", Icons.Default.Quiz, goQuiz) }
        item {
            Text("پیشرفت تو به‌صورت خودکار روی همین دستگاه ذخیره می‌شود.",
                color = Color.Gray, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = Blue, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Navy)
        }
    }
}

@Composable
private fun FeatureCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, action: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { action() }, colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Blue)
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(title, color = Navy, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(Modifier.width(12.dp))
            Icon(icon, contentDescription = null, tint = Blue, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, color = Navy, fontWeight = FontWeight.Bold, fontSize = 18.sp,
        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)
}

@Composable
private fun LessonsPage() {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { SectionTitle("آموزش کامل تشبیه") }
        itemsIndexed(lessons) { index, lesson ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.End) {
                    Text("${index + 1}. ${lesson.first}", color = Blue, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(lesson.second, color = Navy, lineHeight = 26.sp, textAlign = TextAlign.Right)
                }
            }
        }
    }
}

@Composable
private fun ExamplesPage() {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { SectionTitle("نمونه‌های ادبی و آموزشی") }
        item {
            Text("نمونه‌های زیر برای تمرین و شناخت ارکان تشبیه انتخاب شده‌اند؛ جمله‌های آموزشی‌اند و بیت شعر نقل‌شده نیستند.",
                color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Right)
        }
        itemsIndexed(examples) { index, example ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.End) {
                    Text("نمونهٔ ${index + 1}", color = Blue, fontWeight = FontWeight.Bold)
                    Text(example.first, color = Navy, fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 6.dp), textAlign = TextAlign.Right)
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Text(example.second, color = Green, textAlign = TextAlign.Right, lineHeight = 24.sp)
                }
            }
        }
    }
}

@Composable
private fun QuizPage(onCorrect: () -> Unit, onAnswered: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var submitted by remember { mutableStateOf(false) }
    var localScore by remember { mutableIntStateOf(0) }
    val q = quiz[index % quiz.size]
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.End) {
        SectionTitle("آزمون چهارگزینه‌ای")
        Text("سؤال ${(index % quiz.size) + 1} از ${quiz.size}", color = Color.Gray)
        LinearProgressIndicator(progress = { ((index % quiz.size) + 1).toFloat() / quiz.size },
            modifier = Modifier.fillMaxWidth())
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.End) {
                Text(q.question, color = Navy, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                    textAlign = TextAlign.Right, lineHeight = 28.sp)
                Spacer(Modifier.height(12.dp))
                q.options.forEachIndexed { i, option ->
                    val bg = when {
                        submitted && i == q.answer -> Color(0xFFE1F4EA)
                        submitted && i == selected && selected != q.answer -> Color(0xFFFFE7E7)
                        selected == i -> Color(0xFFE6EEFF)
                        else -> Pale
                    }
                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        .background(bg, RoundedCornerShape(12.dp)).clickable { if (!submitted) selected = i }
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected == i, onClick = { if (!submitted) selected = i })
                        Spacer(Modifier.weight(1f))
                        Text(option, color = Navy, textAlign = TextAlign.Right)
                    }
                }
                if (submitted) {
                    Spacer(Modifier.height(10.dp))
                    Text(if (selected == q.answer) "آفرین! پاسخ درست است." else "پاسخ درست: ${q.options[q.answer]}",
                        color = if (selected == q.answer) Green else Color(0xFFB3261E),
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
                    Text(q.explanation, color = Navy, textAlign = TextAlign.Right, lineHeight = 24.sp)
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (!submitted && selected >= 0) {
                            submitted = true
                            onAnswered()
                            if (selected == q.answer) { localScore++; onCorrect() }
                        } else if (submitted) {
                            index++; selected = -1; submitted = false
                        }
                    },
                    enabled = submitted || selected >= 0,
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (submitted) "سؤال بعدی" else "ثبت پاسخ") }
                Text("امتیاز این دور: $localScore", color = Blue,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun GamePage(onCorrect: () -> Unit, onAnswered: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var checked by remember { mutableStateOf(false) }
    var points by remember { mutableIntStateOf(0) }
    val task = gameTasks[index % gameTasks.size]
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.End) {
        SectionTitle("بازی تشخیص ارکان تشبیه")
        Text("گزینهٔ درست را انتخاب کن و امتیاز بگیر.", color = Color.Gray)
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.End) {
                Text(task.question, color = Navy, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right, lineHeight = 28.sp)
                Spacer(Modifier.height(12.dp))
                task.options.forEachIndexed { i, option ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        .background(if (selected == i) Color(0xFFE6EEFF) else Pale, RoundedCornerShape(12.dp))
                        .clickable { if (!checked) selected = i }.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected == i, onClick = { if (!checked) selected = i })
                        Spacer(Modifier.weight(1f))
                        Text(option, color = Navy)
                    }
                }
                if (checked) {
                    Text(if (selected == task.answer) "درست است! +۱ امتیاز" else "پاسخ درست: ${task.options[task.answer]}",
                        color = if (selected == task.answer) Green else Color(0xFFB3261E),
                        fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
                    Text(task.reason, color = Navy, textAlign = TextAlign.Right)
                }
                Button(
                    onClick = {
                        if (!checked && selected >= 0) {
                            checked = true
                            onAnswered()
                            if (selected == task.answer) { points++; onCorrect() }
                        } else if (checked) {
                            index++; selected = -1; checked = false
                        }
                    },
                    enabled = checked || selected >= 0,
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (checked) "مرحلهٔ بعد" else "بررسی پاسخ") }
                Text("امتیاز بازی: $points", color = Blue)
            }
        }
    }
}
