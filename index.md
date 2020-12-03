---
title: Clojure and the M1, notes and a quick comparison
---

# Clojure on the M1: notes and a quick comparison
  

The first wave of Apple's M1-based computers are now out in the wild. Obviously the first question is: "Can I get my clojure dev environment up and running today?"
  
**tl;dr** yes, with surprisingly few caveats, dodges and workarounds. Only one really.
 
So, The Goal: Quickly replicate the existing Clojure development environment present on a trusty 'traveller' laptop, onto a new M1-based Mac Mini.
  
    
### gear used
* 2017 MacBook Air, 8GB memory 128GB SSD:
MBAs, even entry-level machines like this one, have been omnipresent in the dev world for years.

* 2020 Mac Mini, with the M1 SoC, 8GB memory 256GB SSD:
The absolute entry tier to M1-world. 
<hr/>  
  
  
### environment
a fairly minimal web dev setup including:
* OpenJDK
* Leiningen
* Postgresql
* MacVim; with a few clojure-oriented plugins
* git

> Not included here : the unboxing, user and credential setup, etc.

<hr/>
  
  
## setup

### OpenJDK
OK, this is the ONE slight caveat mentioned in the Intro, and naturally is the most important piece.
At the present time there is a [JEP](https://openjdk.java.net/jeps/391) ongoing for OpenJDK M1 port but Azul has already released a [M1 build of OpenJDK](https://www.azul.com/downloads/zulu-community/?package=jdk).

There are various build versions available but for now I'm using that stalwart of the clojure dev ecosystem, JDK1.8, using the bundle version rather than the dmg installer.

So; Download the bundle zip file, verify the sha256 signature, and install as follows:

Look inside the zip and copy the 'zulu-8-x86.jdk' folder.

paste this into /Library/Java/JavaVirtualMachines/

> NOTE: if you have, by some chance, set the JAVA_HOME environment variable you will need to UNSET it.

now, running:
```
 /usr/libexec/java_home
```
should yield:
```
   /Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home
```

and:
```
java -version
```

should yield:
 ```
openjdk version "1.8.0_275"
OpenJDK Runtime Environment (Zulu 8.50.0.1013-CA-macos-aarch64) (build 1.8.0_275-b01)
OpenJDK 64-Bit Server VM (Zulu 8.50.0.1013-CA-macos-aarch64) (build 25.275-b01, mixed mode)
```  

Yay! Java!
<hr/>

**git**\
The first time you type 'git' in a terminal you are prompted to install the XCode CLI dev tools.
This takes just a few minutes of waiting.
The version installed here is Git 2.24.3 (Appple Git-128).

<hr/>

**vi/vim**\
Preinstalled, NOP.

<hr/>

**MacVim**
I use [this](https://macvim-dev.github.io/macvim/) on the MBA and it installed as usual on the Mini. But as it currently is still a Intel x86 binary it will need to run under Rosetta, the x86 -> M1 translation utility.
  
The 'translation' ops are fast (< 1m) and one-time. MacVim then launches normally. As fast as ever.

> If this is the first x86 app you have tried to install you will get a prompt to install Rosetta.\
  And the Activity Monitor utility helpfully includes a *architecture* ["Apple" "Intel"] column.

I completed the editor setup with some config file tweaks and a few favored plugins: [paredit](https://github.com/vim-scripts/paredit.vim), [vim-fireplace](https://github.com/tpope/vim-fireplace), [vim-vinegar](https://github.com/tpope/vim-vinegar) and [solarized](https://github.com/altercation/vim-colors-solarized). As those are vimscript plugins no problems were expected and none occurred.

> 

<hr/>

**Leingingen**
Install via the lein script as per the usual instructions on the Leiningen site.\
I went with the $HOME/bin style of install here. It was completely usual and nominal.

&nbsp;

> - - -
> **READY!** At this point you should have a working Clojure dev environment on your M1 hardware of choice.
> - - -

&nbsp;

**(optional) postgresql**
There are currently a few options for installing this DB, including via Homebrew, compiling from source and GUI apps.
In order to match the MBA environment I used [Postgres.app](https://postgresapp.com). As this is currently still only x86-based it will pass through the Rosetta translations ops upon first run.\
Installs and runs as expected.
  
<hr/>

**Some Maybe Options not addressed here**

* **emacs** ::
Many in the clojure community use emacs, but as of this writing it is apparently not quite ready.
  
* **Homebrew** ::
[Although proceeding rapidly](https://github.com/Homebrew/brew/issues/7857) support is incomplete and at the moment the brew command is available only via Rosetta.
> Command line calls [can use the 'arch -x86_64' prefix](https://github.com/Homebrew/brew/issues/9173) to run apps with Rosetta2.


* **virtualization and containers** ::
These are a bit shaky at the moment, and I'm not covering them as they are not in the workflow of the two 8GB memory minimalist dev setups.
   
<hr/>
  
  
### A quick comparison
The new Mini feels exremely responsive and snappy. How about a quick performance test?

For a more valid comparison I added the [x86_64 build of the Azul JDK1.8](https://www.azul.com/downloads/zulu-community/?package=jdk) to the MBA and set it as the active JVM. 
  
Also 'lein deps' was pre-run on the MBA to assure that the the local Maven (.m2) archives are equivalent with regard to target project..

Still, this is not exactly a fair comparison; the Mini has a bigger fan and is running Big Sur while the MBA is fully patched but not bumped up to this latest OS.
  
Nevertheless...
  


* test 1, uberjar build
Assembling a deployment-ready uberjar from a ~32Kloc website source base:
Literally a current project I was just deploying, cloned onto the new platform. For Science.


runs, averaged & rounded up    |  MBA     | M1 Mini
-------------------------------|----------|--------
10                             | 48 secs  | 17 secs
  
So the M1 Mini required only about 35% of the processing time of the MBA; not too bad.
  
Hardly an epic computation, but this is a basic type of op that many of us do very frequently.
  
<hr/>

* test 2: basic function calls and calculations
Calculation a large number of distances using the Havorsine algorithm.
Again, something from yesterday's workday.:\
A calculation from a single fixed point -["Point Nemo", the Oceanic Pole of Inaccessibility](https://en.wikipedia.org/wiki/Pole_of_inaccessibility)- to a randomly-generated lat/lng corrdinate, repeated many, many times.\
It's a thing that is done.
  
> code snippet included in this repo.

rounds |  MBA     | M1 Mini  | % difference
------ |----------|----------|-------------
3m     |2676 ms   |1536 ms   | ~57%
30m    |26229 ms  |14612 ms  | ~55%
300m   |264782 ms |14560 ms  | ~55%
  
The M1 Mini is somewhat less than twice as fast at this task as the MBA. Nice.

Another decidedly non-cpu-melting benchmark but it looks like Apple is really not fooling around with this latest SoC initiative.

<hr/>
  
  
### epilogue
This lowest-tier M1 Mini is really impressive, and replicating a minimal 'live' clojure dev environment from a previous generation laptop was mostly straightforward. There are still many holes in M1 software support but I expect these will be filled-out in short order.

Even a few trivial performance comparisons suggest notable gains in a daily-driver development situation.
Finally, both the M1 Mini and 2017 MBA used here are low-end machines. It will be very interesting to see what the inevitable 16" MBP M(x) laptop will be able to do. I expect somewhere inside the Ring there are some squee-worthy prototypes. 
    
And although I'd be surprised to see Apple target a product for the datacenter space, one might imagine what could be packed into a 1U, and what the power & cooling reductions might look like there.
  
A welcome distraction in these dark times.
  
    
    
