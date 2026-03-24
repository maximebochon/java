Synthèse des caractères spéciaux communs en Français avec le code Unicode associé,
comme aide à l'édition de fichiers Java `.properties` "à l'ancienne".
Comme l'idée est d'intégrer ce commentaire en tête de fichier `.properties`,
on fait attention de n'utiliser aucun accent :

```
#--------------------------------------------------------------------------------------#
#
#       CODES A UTILISER POUR LES CARACTERES SPECIAUX/ACCENTUES
#
#  Accent grave :               Accent aigu :                Cedille :
#    a=\u00E0  A=\u00C0           e=\u00E9 E=\u00C9            c=\u00E7 C=\u00C7
#    e=\u00E8  E=\u00C8
#
# Accent circonflexe :          Trema :
#   a=\u00E2  A=\u00C2            a=\u00E4  A=\u00C4
#   e=\u00EA  E=\u00CA            e=\u00EB  E=\u00CB
#   i=\u00EE  I=\u00CE            i=\u00EF  I=\u00CF
#   o=\u00F4  O=\u00D4            o=\u00F6  O=\u00D6
#   u=\u00FB  U=\u00DB            u=\u00FC  U=\u00DC
#
# Guillemet :                   Apostrophe :
#   gauche=\u00AB                 dactylographique=\u0027
#   droit=\u00BB                  typographique=\u2019
#
# Tiret :                       Divers :
#   demi-cadratin=\u2013          euro=\u20AC
#   cadratin=\u2014               multiplication=\u00D7
#
#--------------------------------------------------------------------------------------#
```
