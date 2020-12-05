package com.example.ar_car_test

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import android.widget.Toast
import com.google.ar.sceneform.ux.TransformableNode
import android.view.MotionEvent
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode


class MainActivity : AppCompatActivity() {

    private var arCoreFragment: ArFragment? = null
    private var renderableObj: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arCoreFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        buildRenderable()

        setListeners()
    }

    private fun buildRenderable() {
        ModelRenderable.builder()
            .setSource(this, Uri.parse("Convertible.sfb"))
            .build()
            .thenAccept { renderable -> this.renderableObj = renderable }
            .exceptionally {
                Log.e("MainActivity123", it.toString())
                Toast.makeText(this, "Unable to load andy renderableObj", Toast.LENGTH_LONG).show()
                null
            }
    }

    private fun setListeners() {
        arCoreFragment?.apply {
            setOnTapArPlaneListener { hitresult: HitResult, plane: Plane, motionevent: MotionEvent ->
                if (renderableObj == null) {
                    return@setOnTapArPlaneListener
                }

                val anchor = hitresult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arSceneView.scene)

                val lamp = TransformableNode(transformationSystem)
                lamp.setParent(anchorNode)
                lamp.renderable = renderableObj
                lamp.select()
            }
        }

    }
}