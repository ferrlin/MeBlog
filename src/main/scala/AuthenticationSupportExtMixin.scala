package net.ironthreadworks

import org.scalatra.{ScalatraKernel,FlashMapSupport,CookieSupport}
import org.scalatra.auth.{ScentrySupport}

case class BlogUser(id: String)

trait AuthencaticationSupport 
		extends ScentrySupport[BlogUser]
							with FlashMapSupport
							with CookieSupport { self: ScalatraKernel =>

		before { scentry.authenticate('RememberMe) }

		override protected def registerAuthStrategies = {
			scentry.registerStrategy('UserPassword, app => null /*new UserPasswordStrategy(app)*/)
			scentry.registerStrategy('RememberMe, app => null /*new RememberMeStrategy(app) */)
		}

		protected def fromSession = {
			case id: String => BlogUser(id)
		}

		protected def toSession = {
			case user: BlogUser => user.id
		}
}
