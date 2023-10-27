import android.content.Context
import android.content.SharedPreferences

class CambiarTema {
    companion object {
        private const val PREF_NAME = "TemaPreferences"
        private const val KEY_TEMA_ID = "temaId"

        fun Guardar(context: Context, temaId: Int) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(KEY_TEMA_ID, temaId)
            editor.apply()
        }

        fun Cargar(context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val id = sharedPreferences.getInt(KEY_TEMA_ID, 0)

            // Cambiar el tema de la aplicación antes de super.onCreate
            context.setTheme(id)
        }
    }
}