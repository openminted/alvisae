  ant -f AlvisAE.shared/build.xml jar
  ant -f AlvisAE.Core/build.xml build
  ant -f AlvisAE.Core/build.xml distribute
  ant -f AlvisAE.Editor/build.xml build
  ant -f AlvisAE.Editor/build.xml distribute
  ant -f AlvisAE.TyDI-Ext/build.xml build
  ant -f AlvisAE.TyDI-Ext/build.xml distribute
  ant -f AlvisAE.GenericUI/build.xml war
