package udl.eps.demos.basedatosmenucompleto_kotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import udl.eps.demos.basedatosmenucompleto_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SQLiteDatabase

    @SuppressLint("Recycle", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        val usdbh = UsuariosSQLiteHelper(this, "DBUsuarios", null, 1)
        db = usdbh.getWritableDatabase()
        binding.btnInsertar.setOnClickListener { //Recuperamos los valores de los campos de texto
            val nom = binding.txtNom.getText().toString()
            val tel = binding.txtTel.getText().toString()
            val mail = binding.txtMail.getText().toString()
            val nuevoRegistro = ContentValues()
            nuevoRegistro.put("nombre", nom)
            nuevoRegistro.put("telefono", tel)
            nuevoRegistro.put("email", mail)
            if (db.insert(
                    "Usuarios",
                    null,
                    nuevoRegistro
                ) > 0
            ) showToast(getString(R.string.correctIn)) else showToast(getString(R.string.errIn))
        }

        binding.btnActualizar.setOnClickListener { //Recuperamos los valores de los campos de texto
            val nom = binding.txtNom.getText().toString()
            val tel = binding.txtTel.getText().toString()
            val mail = binding.txtMail.getText().toString()
            val args = arrayOf(nom)
            val valores = ContentValues()
            valores.put("telefono", tel)
            valores.put("email", mail)
            val cont = db.update("Usuarios", valores, "nombre=?", args)
            if (cont > 0) showToast(
                getString(R.string.correctAct) + Integer.toString(cont)
            ) else showToast(getString(R.string.noReg))
        }

        binding.btnEliminar.setOnClickListener { //Recuperamos los valores de los campos de texto
            val nom = binding.txtNom.getText().toString()
            val args = arrayOf(nom)
            val cont = db.delete("Usuarios", "nombre=?", args)
            if (cont > 0) showToast(getString(R.string.correctEl) + Integer.toString(cont))
            else showToast(getString(R.string.noReg))
        }

        binding.btnConsultar.setOnClickListener {
            //Alternativa 1: metodo rawQuery()
            val c = db.rawQuery("SELECT nombre, telefono, email FROM Usuarios", null)

            //Alternativa 2: metodo query()
            //String[] campos = new String[] {"nombre", "telefono", "email"};
            //Cursor c = db.query("Usuarios", campos, null, null, null, null, null);

            //Recorremos los resultados para mostrarlos en pantalla
            binding.txtResultado.setText("")
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya mas registros
                do {
                    val nom = c.getString(0)
                    val tel = c.getString(1)
                    val mail = c.getString(2)
                    binding.txtResultado.append(" $nom - $tel - $mail\n")
                } while (c.moveToNext())
            } else binding.txtResultado.setText(getString(R.string.tablaV))
        }

        binding.btnConsultar1.setOnClickListener {
            val campos = arrayOf("telefono", "email")
            val nom = binding.txtNom.getText().toString()
            val args = arrayOf(nom)
            val c = db.query("Usuarios", campos, "nombre=?", args, null, null, null)
            binding.txtResultado.setText("")

            //Alternativa 1: metodo rawQuery()
            //String nom = txtNombre.getText().toString();
            //String[] args = new String[]{nom};
            //Cursor c = db.rawQuery("SELECT telefono, email FROM Usuarios WHERE nombre=?", args);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya mas registros
                do {
                    val tel = c.getString(0)
                    val mail = c.getString(1)
                    binding.txtResultado.append(" $tel - $mail\n")
                } while (c.moveToNext())
            } else binding.txtResultado.setText(R.string.noReg)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }
}
