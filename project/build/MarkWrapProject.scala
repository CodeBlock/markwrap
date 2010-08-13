/*
  ---------------------------------------------------------------------------
  This software is released under a BSD license, adapted from
  http://opensource.org/licenses/bsd-license.php

  Copyright (c) 2010 Brian M. Clapper
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are
  met:

  * Redistributions of source code must retain the above copyright notice,
    this list of conditions and the following disclaimer.

  * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.

  * Neither the names "clapper.org", "Grizzled Scala Library", nor the
    names of its contributors may be used to endorse or promote products
    derived from this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
  IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  ---------------------------------------------------------------------------
*/

import sbt._

import java.io.File

/**
 * Project build file, for SBT.
 */
class MarkWrapScalaProject(info: ProjectInfo)
    extends DefaultProject(info)
    with posterous.Publish
{
    /* ---------------------------------------------------------------------- *\
                         Compiler and SBT Options
    \* ---------------------------------------------------------------------- */

    override def compileOptions = Unchecked :: super.compileOptions.toList

    // 2.8 and xsbt: Override documentOptions, because they're for 2.7, not
    // 2.8, and they've changed.
    private val docTitle = projectName + " " + projectVersion + " API"
    override def documentOptions = List(CompoundDocOption("-doc-title",
                                                          docTitle))
                                                     
    override def parallelExecution = true // why not?

    /* ---------------------------------------------------------------------- *\
                                   Tasks
    \* ---------------------------------------------------------------------- */

    // Override the default "package" action to make it dependent on "test"
    // and "doc".
    override def packageAction = super.packageAction.dependsOn(test, doc)

    /* ---------------------------------------------------------------------- *\
                                Publishing
    \* ---------------------------------------------------------------------- */

    lazy val home = Path.fileProperty("user.home")
    lazy val publishTo = Resolver.sftp("clapper.org Maven Repo",
                                       "maven.clapper.org",
                                       "/var/www/maven.clapper.org/html") as
                         ("bmc", (home / ".ssh" / "id_dsa").asFile)

    override def managedStyle = ManagedStyle.Maven

    /* ---------------------------------------------------------------------- *\
                       Managed External Dependencies
    \* ---------------------------------------------------------------------- */

    // Repositories
    val newReleaseToolsRepository = ScalaToolsSnapshots
    val t_repo = "t_repo" at
        "http://tristanhunt.com:8081/content/groups/public/"
    val snug_repo = "uk-releases" at "http://www2.ph.ed.ac.uk/maven2/"

    // Artifacts

    val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"

    val wikitext = "org.eclipse.mylyn.wikitext" % "wikitext.textile" %
                   "0.9.4.I20090220-1600-e3x"
    val knockoff = "com.tristanhunt" %% "knockoff" % "0.7.3-14"
    // snuggletex-core is needed by knockoff
    val snuggletex = "uk.ac.ed.ph.snuggletex" % "snuggletex-core" % "1.2.2"

    /* ---------------------------------------------------------------------- *\
                          Private Helper Methods
    \* ---------------------------------------------------------------------- */

}
