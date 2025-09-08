
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.ReaderWriter

object ReaderConfig:
  /*
  final case class Reader[E, A](run: E => A) {
    def map[B](f: A => B): Reader[E, B] =
      Reader(e => f(run(e)))

    def flatMap[B](f: A => Reader[E, B]): Reader[E, B] =
      Reader(e => f(run(e)).run(e))
 }

  * */
  import cats.data.Reader

  /*
  * Reader[E, A] represents a computation that needs an environment of type E to produce a result of type A.
  * Instead of passing the environment explicitly to every function, you can compose Readers and supply the environment
  * once at the end.
  * */
  case class AppConfig(dbUrl: String, apiKey: String)
  case class User(id: Int, name: String)

  class Database {
    def getUser(id: Int): User = User(id, s"user-$id")
  }

  class ApiService {
    def enrich(user: User, apiKey: String): User =
      user.copy(name = s"${user.name}-$apiKey")
  }

  // Environment that holds dependencies
  //  We bundle together all dependencies into one environment
  case class Env(config: AppConfig, db: Database, api: ApiService)

  /*
  * Reader(env => ...) wraps a function Env => A.
  * fetchUser(42) produces a Reader[Env, User] that, when run, will call env.db.getUser(42).
  * enrichUser(user) produces a Reader[Env, User] that, when run, will call the API service with the environment’s config.
  * */
  val fetchUser: Int => Reader[Env, User] =
    id => Reader(env => env.db.getUser(id))

  val enrichUser: User => Reader[Env, User] =
    user => Reader(env => env.api.enrich(user, env.config.apiKey))

  // So program(42) is a Reader[Env, User] that means:
  //Use the environment to fetch a user by ID.
  //With the same environment, enrich that user with the API service.
  //Return the enriched user.
    /*
    def program(userId: Int): Reader[Env, User] =
      fetchUser(userId).flatMap { user: User =>
        enrichUser(user).map { enriched: User =>
          enriched
        }
      }

    Reader { env =>
      // Step 1: run the first Reader
      val user: User = fetchUser(userId).run(env)

      // Step 2: apply f (the lambda) to get another Reader
      val reader2: Reader[Env, User] = enrichUser(user).map(enriched => enriched)

      // Step 3: run the second Reader with the same env
      reader2.run(env)
    }

          fetchUser(userId)                  enrichUser(user)
        Reader(env => env.db.getUser(id))    Reader(env => env.api.enrich(...))
                      │                                 │
                      │ run(env)                        │ run(env)
                      ▼                                 ▼
                User(id, "user-id")  ───► passed ───►  User(id, "user-id-APIKEY")

fetchUser(userId) returns a Reader[Env, User].
.flatMap unwraps that Reader by running it with the environment: fetchUser(userId).flatMap { user: User =>
    def flatMap[B](f: A => Reader[E, B]): Reader[E, B] =
      Reader(e => f(run(e)).run(e))
The user: User produced is passed into your function.

Your function returns another Reader[Env, User].

flatMap immediately runs that second Reader with the same env.

The whole thing is wrapped again as a new Reader[Env, User].
    * */
  def program(userId: Int): Reader[Env, User] = for {
//    If we call fetchUser(42), we get back a value of type: Reader[Env, User].
//This is not a User. It’s a description of a computation that, given an Env, will eventually produce a User.
    user <- fetchUser(userId)
    enriched <- enrichUser(user)
  } yield enriched

  /*
    fetchUser(userId).flatMap { user: User =>
      enrichUser(user).map { enriched =>
        enriched
      }
    }

    def flatMap[B](f: A => Reader[E, B]): Reader[E, B] =
      Reader(e => f(run(e)).run(e))

    E = Env
    A = User (the result of fetchUser)
    B = User (the result of enrichUser)

    fetchUser(42) = Reader(env => env.db.getUser(42))
    Reader(env => env.db.getUser(42)).flatMap[B](f: A => Reader[E, B]): Reader[E, B] = Reader(e => f(run(e)).run(e))
    Reader(env => env.db.getUser(42)).flatMap { user: User => enrichUser(user).map(enriched => enriched) }
    so f === user: User => enrichUser(user).map(enriched => enriched)
    run: E => A is the function wrapped by the current Reader.
    We call run(e) to get a value of type A === User.
    Then we apply f to that A.
    Since f: A => Reader[E, B], applying it gives a new Reader[E, B].
    We immediately .run(e) that new Reader, with the same environment.
    run(e) = e.db.getUser(42) → returns User(42, "user-42")
    f(User(42, "user-42")) = enrichUser(User(42, "user-42")).map(enriched => enriched)
  * */

  @main def runReaderConfig(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/ReaderWriter/ReaderConfig.scala created at time 5:24PM")
    val env = Env(AppConfig("jdbc://db", "XYZ123"), new Database, new ApiService)
    val result = program(42).run(env)
    println(result) // User(42, "user-42-XYZ123")

