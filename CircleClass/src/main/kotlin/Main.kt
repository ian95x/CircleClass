// Create a class named Circle that draws a circle with some radius at the position it was created.
// Create 10 instances of this class at random positions of the screen with random radius,
// and also with an interval of 0.25 seconds between the creation of each instance.
// After all instances are created (so after 2.5 seconds) start deleting once random instance every [0.5, 1]
// second (a random number between 0.5 and 1). After all instances are deleted, repeat the entire process of
// recreation of the 10 instances and their eventual deletion

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

class MyCircle(private val x:Int, private val y:Int, private val radius:Int){
    fun draw (g:Graphics2D){
        g.fillOval(x- radius, y-radius,radius * 2, radius * 2)
    }
}

class CirclePanel: JPanel() {
    private val myCircle = mutableListOf<MyCircle>()

    init{
        preferredSize = Dimension(700,700)
        background = Color.white

        fixedRateTimer("MyCircleCreationTimer", true, 0L,250L){
            if(myCircle.size <10) {
                val x= Random.nextInt(width)
                val y= Random.nextInt(height)
                val radius = Random.nextInt(10, 30)
                myCircle.add(MyCircle(x,y,radius))
                repaint()
            }
        }
        fixedRateTimer("MyCircleDeletionTimer", true,2500L, Random.nextLong(250,500))
        {
            if (myCircle.isNotEmpty()){
                val randomIndex = Random.nextInt(myCircle.size)
                myCircle.removeAt(randomIndex)
                repaint()
            }
        }


    }
    override fun paintComponent(g:Graphics){
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
        myCircle.forEach{it.draw(g2d)}
        Toolkit.getDefaultToolkit().sync()
    }
}

fun main() {
    val frame = JFrame("Circle Animation")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.contentPane.add(CirclePanel())
    frame.pack()
    frame.isVisible = true
}



