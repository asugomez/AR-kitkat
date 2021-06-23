package com.ec.ardesignkitkat.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.FurnitureRepository
import com.ec.ardesignkitkat.data.model.Furniture
import com.ec.ardesignkitkat.ui.main.adapter.FurnitureAdapter
import com.ec.ardesignkitkat.ui.main.viewmodel.FurnViewModel
import com.vikramezhil.droidspeech.OnDSListener
import com.vikramezhil.droidspeech.OnDSPermissionsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ListFurnitureActivity : AppCompatActivity() {

    private var pseudo_user: String? = null
    private var hash: String? = null
    private var id_user: String? = null
    private var progress: View? = null
    private var list: View? = null
    private var btnPartager: Button? = null

    private var furnitures: MutableList<Furniture> ?= null
    //private var walls: MutableList<Wall> ?= null
    //private var stand_furnitures: MutableList<StandardFurniture> ?= null
    var TAG = "ARDesign furnitures"

    private var recyclerView: RecyclerView?= null
    private var adapter: FurnitureAdapter? = null

    private val furnViewModel by viewModels<FurnViewModel>()

    val furnitureRepository by lazy { FurnitureRepository.newInstance(application) }
    //val wallRepository by lazy { WallRepository.newInstance(application) }
    //val standFurnRepository by lazy { StandardFurnitureRepository.newInstance(application) }

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mes_objets)
        initializeVariables()
        if(hash!=null){
            setUpRecyclerView()
        }
        //changeToctivity()
    }

    fun initializeVariables(){
        // récuperation des données
        hash = intent.getStringExtra("hash")
        id_user = intent.getStringExtra("id_user")
        pseudo_user = intent.getStringExtra("pseudo_user")
        progress = findViewById(R.id.progressBarCh)
        list = findViewById(R.id.mRecycler)
        btnPartager = findViewById(R.id.btnPartager)
        partager()
        /*btnPartager?.setOnClickListener{
            partager()
        }*/

        if(pseudo_user!=null){
            this.title = "Meubles de $pseudo_user"
        }else{
            this.title = "Vous n'êtes pas enregistré !"
            showProgress(false)
        }



        activityScope.launch{
            try{
                if(hash!=null){
                    val furns = furnitureRepository.getUsersFurnitures(idUser = id_user!!.toInt(), hash!!)
                    adapter!!.addData(furns)
                }
            } catch (e: Exception){
                Toast.makeText(this@ListFurnitureActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun partager(){
        /*adapter?.setOnItemClickListener(object: FurnitureAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val nom_furn = furnitures?.get(position)?.nom
                val id_furn = furnitures?.get(position)?.id
                val l_furn = furnitures?.get(position)?.length
                val w_furn = furnitures?.get(position)?.width
                val h_furn = furnitures?.get(position)?.height
                // todo create qr code
            }
        })*/
    }

    fun setUpRecyclerView(){
        furnitures = mutableListOf()
        //walls = mutableListOf()
        //stand_furnitures = mutableListOf()

        recyclerView = findViewById(R.id.mRecycler)

        adapter = FurnitureAdapter(furnitures!!)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        recyclerView?.visibility = View.GONE
        //Toast.makeText(this@ListFurnitureActivity, "appel a load furnitures", Toast.LENGTH_SHORT).show()
        loadFurnitures()
        recyclerView?.visibility = View.VISIBLE
    }


    fun loadFurnitures() {
        try{
            if(hash!=null){
                //Log.v(TAG,"HASH NOT NULL")
                //Toast.makeText(this@ListFurnitureActivity, "load meubles", Toast.LENGTH_SHORT).show()
                furnViewModel.getUserFurnitures(id_user!!.toInt(),hash!!)
                furnViewModel.furnitures.observe(this) { viewState ->
                    when (viewState) {
                        is FurnViewModel.ViewState.Content -> {
                            //Log.v(TAG,"HERE view content")
                            showProgress(false)
                        }
                        FurnViewModel.ViewState.Loading -> showProgress(true)
                        is FurnViewModel.ViewState.Error -> {
                            Toast.makeText(this@ListFurnitureActivity, "ERROR in load meubles", Toast.LENGTH_SHORT).show()
                            showProgress(false)
                            Toast.makeText(this@ListFurnitureActivity, "${viewState.message} ", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                }

            }
        }catch(e: Exception){
            Toast.makeText(this@ListFurnitureActivity,"${e.message} ", Toast.LENGTH_SHORT).show()
        }
    }

    fun addFurniture(){
        activityScope.launch {
            try {
                if(id_user!=null && hash!=null){
                    recyclerView?.visibility = View.GONE
                    Toast.makeText(this@ListFurnitureActivity, "add furniture", Toast.LENGTH_SHORT).show()

                    val width = 100
                    val height = 200
                    val length = 120
                    val nom = "test"
                    furnitureRepository.addUsersFurniture(id_user!!.toInt(), width.toString(), height.toString(), length.toString(), nom, hash!!)
                    val furnitures = furnitureRepository.getUsersFurnitures(id_user!!.toInt(), hash!!)
                    adapter!!.addData(furnitures)

                    recyclerView?.visibility = View.VISIBLE
                }

            }catch (e:Exception){
                Toast.makeText(this@ListFurnitureActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showProgress(show: Boolean) {
        progress?.isVisible = show
        list?.isVisible = !show
    }

}