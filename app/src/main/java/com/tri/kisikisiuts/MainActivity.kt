package com.tri.kisikisiuts

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import android.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        title = "AL Mart"


        val etNmBarang = findViewById<EditText>(R.id.etNmBarang)
        val etHarsat = findViewById<EditText>(R.id.etHarsat)
        val etQty = findViewById<EditText>(R.id.etQty)
        val lvBarang = findViewById<ListView>(R.id.lvBarang)
        val btnTambah = findViewById<Button>(R.id.btnTambah)
        val chPotongan = findViewById<CheckBox>(R.id.chPotongan)
        val chBonus = findViewById<CheckBox>(R.id.chBonus)
        val btnProses = findViewById<Button>(R.id.btnProses)

        var totQty = 0
        var totHarga = 0
        val daftarBarang = arrayListOf<String>()

        btnTambah.setOnClickListener {
            if("${etNmBarang.text}".trim().isNotEmpty() &&
                "${etHarsat.text}".trim().isNotEmpty() && "${etQty.text}".trim().isNotEmpty()) {
                val nmBarang = "${etNmBarang.text}".trim()
                val harsat = "${etHarsat.text}".trim().toInt()
                val qty = "${etQty.text}".trim().toInt()
                val subtotal = qty * harsat
                totQty += qty
                totHarga += subtotal
                val infoBarang = """ 
                    $nmBarang 
                    Harga satuan: ${String.format("%,d", harsat)} 
                    Jumlah beli: ${String.format("%,d", qty)} 
                    Subtotal: ${String.format("%,d", subtotal)} 
                """.trimIndent()
                daftarBarang.add(infoBarang)
                val adp = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1,
                    daftarBarang)
                lvBarang.adapter = adp
                etNmBarang.setText("")
                etHarsat.setText("")
                etQty.setText("")
                etNmBarang.requestFocus()
            } else
                Toast.makeText(this@MainActivity, "Data barang belum lengkap",
                    Toast.LENGTH_SHORT).show()
        }

        btnProses.setOnClickListener {

            val potongan = if (chPotongan.isChecked) "(-50%)" else ""
            val totHargaPotongan = totHarga / if (chPotongan.isChecked) 2 else 1
            var struk = """ 
                Total barang yang dibeli: ${String.format("%,d", totQty)} 
                Total harga: ${String.format("%,d", totHargaPotongan)} $potongan 
            """.trimIndent()
            val bonus = when (totHargaPotongan) {
                in 0..99_999 -> "Tidak ada"
                in 100_000..199_999 -> "Gelas cantik"
                else -> "Piring berlian"
            }
            if (chBonus.isChecked) struk += "\nBonus: $bonus"
            struk +=
                "\n\nSilahkan lanjutkan ke pembayaran untuk mengambil bonus (jika diklaim dan ada)"

            val alb = AlertDialog.Builder(this@MainActivity)
            with(alb) {
                setTitle("Informasi")
                setMessage(struk)
                setCancelable(false)
                setPositiveButton("Lanjut ke pembayaran") { _, _ ->
                    Toast.makeText(
                        this@MainActivity, "Silahkan lakukan pembayaran di kasir",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setNegativeButton("Perbarui daftar belanja", null)
                setNeutralButton("Batal belanja") { _, _ ->
                    Toast.makeText(
                        this@MainActivity,
                        "Terima kasih! Mohon maaf atas ketidaknyamanannya",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                create().show()
            }
        }

    }
}