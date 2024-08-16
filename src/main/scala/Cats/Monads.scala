package Cats

import Cats.Monads.{JobAppMonad, JobApplication, JobBulletinBoard, JobPosting}

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object Monads:

  import cats.Monad
  import cats.syntax.all.*

//  First, we declare some data type and create some data structures that use this type
  type CompanyName = String
  case class JobPosting(position: String, salary: Float)
  case class JobApplication(company: CompanyName, position: String, salary: Float)
  type JobBulletinBoard = Map[CompanyName, JobPosting]

//  Next, we define the monadic wrapper
  case class JobAppMonad[T](jobChoice:T)

// and then we monadize it
  given Monad[JobAppMonad] with {
    override def flatMap[A, B](fa: JobAppMonad[A])(f: A => JobAppMonad[B]): JobAppMonad[B] = f(fa.jobChoice)

    override def tailRecM[A, B](a: A)(f: A => JobAppMonad[Either[A, B]]): JobAppMonad[B] = f(a) match
      case JobAppMonad(x) => x match {
        case Left(a) => tailRecM(a)(f)
        case Right(b) => JobAppMonad(b)
      }
    override def pure[A](x: A): JobAppMonad[A] = JobAppMonad(x)
  }

// this is the data structure that keeps job descriptions
  val bulletinBoard: JobBulletinBoard = Map[CompanyName, JobPosting](
    "ibm" -> JobPosting("software engineer", 150000.25),
    "ibm" -> JobPosting("manager I", 227000.25),
    "twitter" -> JobPosting("janitor", 23000.18)
  )

// we need to define an abstract data type that contains operations on job data
// this data type, MatchApplicants2Jobs is parameterized with a monad data type
// and one of its operations transform the input data into a monad that is parameterized
// by JobPosting and then the other operation is applied to the data in that monad to
// produce a monad that contains the result of the job application.
  trait MatchApplicants2Jobs[M[_]]:
    def jobMatches(bb: JobBulletinBoard, range: (Float, Float)): M[JobPosting]
    def apply4Job(job: JobPosting): M[String]

// this is an instantiation of the abstract data type parameterized by JobAppMonad
  object CareerService extends MatchApplicants2Jobs[JobAppMonad]:
    override def jobMatches(bb: JobBulletinBoard, range: (Float, Float)): JobAppMonad[JobPosting] =
      val jp =  for {
                jobEntry <- bb
                  if jobEntry._2.salary >= range(0) && jobEntry._2.salary <= range(1)
      } yield JobPosting(jobEntry._2.position, jobEntry._2.salary)
      if jp.toList.nonEmpty then JobAppMonad(jp.head) else JobAppMonad(JobPosting("unavailable",0f))

    override def apply4Job(job: JobPosting): JobAppMonad[String] = JobAppMonad(s"Applied for a position of ${job.position}")

  // this operation shows how to use the monad in a comprehension
  def sendJobApplications[M[_]: Monad](jobEngine: MatchApplicants2Jobs[M], min: Float, max: Float): M[String] = for {
    jp <- jobEngine.jobMatches(bulletinBoard, (min, max))
    app <- jobEngine.apply4Job(jp)
  } yield app

  @main def runMain_Monads$(): Unit =
    val res = for {
      v1 <- JobAppMonad(JobApplication("ibm", "software engineer", 150000.25))
      v2 <- JobAppMonad(JobApplication("twitter", "janitor", 23000.18))
    } yield (v1,v2)

    println(res)

    val res1 = sendJobApplications(CareerService, 150000, 180000)
    println(res1)

    val jobs = for {
      jobmatch <- CareerService.jobMatches(bulletinBoard, (150000,250000))
      applied <- CareerService.apply4Job(jobmatch)
    } yield applied
    println(jobs)