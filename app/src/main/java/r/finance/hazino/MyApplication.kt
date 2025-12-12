package r.finance.hazino

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // 👈 This triggers Hilt code generation
class MyApplication : Application()