package com.ec.ardesignkitkat.ui.main

import android.os.Bundle
import android.view.View
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ListFurnitureActivity : AppCompatActivity() {

    private var pseudo: String? = null
    private var hash: String? = null
    private var id_user: String? = null

    private var furnitures: MutableList<Furniture> ?= null
    //private var walls: MutableList<Wall> ?= null
    //private var stand_furnitures: MutableList<StandardFurniture> ?= null

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
        setUpRecyclerView()
        changeToObjectActivity()
    }

    fun setUpRecyclerView(){
        furnitures = mutableListOf()
        //walls = mutableListOf()
        //stand_furnitures = mutableListOf()

        recyclerView = findViewById<RecyclerView>(R.id.mRecycler)

        adapter = FurnitureAdapter(furnitures!!)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        recyclerView?.visibility = View.GONE
        loadFurnitures()
        recyclerView?.visibility = View.VISIBLE
    }

    fun loadFurnitures() {
        try{
            if(hash!=null){
                Toast.makeText(this@ListFurnitureActivity, "load meubles", Toast.LENGTH_SHORT).show()
                furnViewModel.getUserFurnitures(id_user!!.toInt(),hash!!)
                furnViewModel.furnitures.observe(this) { viewState ->
                    when (viewState) {
                        is FurnViewModel.ViewState.Content -> {
                            showProgress(false)
                        }
                        FurnViewModel.ViewState.Loading -> showProgress(true)
                        is FurnViewModel.ViewState.Error -> {
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
        val progress = findViewById<View>(R.id.progressBarCh)
        val list = findViewById<View>(R.id.mRecycler)
        progress.isVisible = show
        list.isVisible = !show
    }

}