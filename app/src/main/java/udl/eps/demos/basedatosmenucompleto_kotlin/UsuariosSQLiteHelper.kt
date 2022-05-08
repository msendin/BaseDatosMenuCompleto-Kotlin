package udl.eps.demos.basedatosmenucompleto_kotlin

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class UsuariosSQLiteHelper(context: Context?, nombre: String?, factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, nombre, factory, version) {

    //Sentencia SQL para crear la tabla de Usuarios
    var sqlCreate = "CREATE TABLE Usuarios " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " nombre TEXT, " +
            " telefono TEXT, " +
            " email TEXT )"

    override fun onCreate(db: SQLiteDatabase) {
        //Se ejecuta la sentencia SQL de creacion de la tabla
        db.execSQL(sqlCreate)

        //Insertamos 5 clientes de ejemplo
        for (i in 1..5) {
            //Generamos los datos de muestra
            val nombre = "Usuario$i"
            val telefono = "688-555-981$i"
            val email = "correo$i@mail.com"


            //Insertamos los datos en la tabla Clientes
            db.execSQL(
                "INSERT INTO Usuarios (nombre, telefono, email) " +
                        "VALUES ('" + nombre + "', '" + telefono + "', '" + email + "')"
            )
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase, versionAnterior: Int,
        versionNueva: Int
    ) {
        //NOTA: Por simplicidad del ejemplo aqui utilizamos directamente
        //      la opcion de eliminar la tabla anterior y crearla de nuevo
        //      vacia con el nuevo formato.
        //      Sin embargo lo normal sera que haya que migrar datos de la
        //      tabla antigua a la nueva, por lo que este metodo deberia
        //      ser mas elaborado.

        //Se elimina la version anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Usuarios")

        //Se crea la nueva version de la tabla
        db.execSQL(sqlCreate)
    }
}