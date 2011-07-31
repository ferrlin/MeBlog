package net.ironthreadworks.meblog

import org.scalatra._
import org.scalatra.scalate._
import com.mongodb.casbah.Imports._
import scala.util.control.Exception._
import scala.collection.mutable.ListBuffer
import scala.actors.Actor.loop
import scala.actors.Actor.react

import java.util.{Date}
import net.ironthreadworks.util.DateTimeHelpers._
import net.ironthreadworks.util.DateTimeHelpers
import net.ironthreadworks.BasicAuthExample.AuthenticationSupport

class BlogServlet extends ScalatraServlet with ScalateSupport with AuthenticationSupport {
	
	val missing = "http://cdn2.iconfinder.com/data/icons/august/PNG/Help.png"
	val mongo = MongoConnection("ferLYNX2")
	val coll = mongo("ferlin")("blog")
	val buffer = new ListBuffer[DBObject]

	def add(info: DBObject = null) {
		//if(info != null) buffer += info
		//if(buffer.size > 0 && (info == null || buffer.length >= 1000)) {
		//	coll.insert(buffer.toList)
			coll.insert(info)	
		//buffer.clear
		//}
	}

	def act() {
		loop {
			react {
				case info: MongoDBObject => add(info.clone())
				case msg if msg == "closeConnection" =>
					add()
					mongo.close
			}
		}
	}
	
	before { 
		contentType = "text/html"
	}

	override protected def contextPath = request.getContextPath
	
	get() {
		val data = coll.find()
		var blog = MongoDBObject()
		/*for(en <- data){
			print(en.get("_id"))
			println(en.get("body"))
		}	*/
		templateEngine.layout("/WEB-INF/layout/list.scaml",Map("blogs"->data.toList.reverse))
	}

	get("/post/:uid"){
		val postId: Option[String] = Option(params("uid"))
		val objectId: ObjectId = new ObjectId(postId.getOrElse().asInstanceOf[String])
		val q = MongoDBObject("_id" -> objectId)
		val obj = coll.findOne(q)
		var map = Map.empty[String,Option[String]]	
		for(ins<-obj) {
		
			map = Map(
					"id" -> ins.getAs[String]("_id"),
					"author" -> ins.getAs[String]("author"),
					"body"-> ins.getAs[String]("body"),
					"title"-> ins.getAs[String]("title"),
					"date" -> ins.getAs[String]("date"),
					"time" -> ins.getAs[String]("time")
					)
		}
		map
		//for(x <- cursor) x
	}

	get("/logout") {
		logout
		<h3>You are logged out!!!</h3>
	}
		
	get("/add") {
		basicAuth
		templateEngine.layout("WEB-INF/layout/blogform.scaml")
	}

	/*get("/:oid"){
			val postId = params("oid")
			
<html><body><h2>Hello Everyong</h2></body></html>
			//coll.find()
	}*/

	post("/add") {

		val tags:String = params("tags")//.split(" \t\r\f\n")

		for(tag <- tags){
			println(tag)
		}

		val categories = params("categories").split(" \t\r\f\n")
		val author = "John"
		
		val now = DateTimeHelpers.format(new Date, "EEEEEEEE, d MMMMM yyyy") // pattern should be like this : Monday, 02 May 2011
		val time = DateTimeHelpers.format(new Date, "HH:mm")
		val tagLinks = ("#","#","#")

		val data: DBObject = Map("title"-> params("title"),"body"-> params("content"),"tags"->tags,"categories"-> categories,"author"-> author, "date"-> now ,"time"-> time,"links" -> tagLinks)
		add(data)
		redirect("/blog/")	
	}
}

