package de.jensklingenberg.ktoronandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    var applicationEngine: ApplicationEngine =
        embeddedServer(
            Netty,
            port = 8083,
            module = {
                ktorApplicationModule()
            })

    val compositeDisposable=CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Completable.fromCallable {
            try {
                applicationEngine.start(wait = true)
            }catch (exception:Exception){
                Log.d("Server", exception.message)
                applicationEngine.stop(0L, 0L, TimeUnit.SECONDS)

            }

            true
        }
            .subscribeOn(Schedulers.io())
            .subscribeBy(onError = {
                Log.d("Server", it.message)
                applicationEngine.stop(0L, 0L, TimeUnit.SECONDS)
            }).addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
