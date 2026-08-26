# Tests en échec sous Windows pour raison d'encodage

## Contexte

- Java 11
- JUnit 5 / Surefire 2.22.2
- utilisation de fichiers ressources pendant les tests pour charger des données SQL en mémoire

## Symptômes

Les tests JUnit sont :
- en succès dans IntelliJ
- en succès en intégration continue sous Jenkins
- en échec en ligne de commandes depuis GitBash sous Windows

Les échecs portent sur des différences sur les caractères spéciaux entre l'attendu,
par exemple `élément`, et l'obtenu, par exemple `Ã©lÃ©ment`,
ce qui correspond typiquement à un décodage incorrect d'un texte UTF-8.

Pourtant, les deux fichiers (Java et SQL) sont bien encodés en UTF-8. 

## Solution

Il est possible de forcer l'interprétation UTF-8 de différentes manières.

## Locale à chaque classe de test

```java
@Sql(
  config = @SqlConfig(encoding = "utf-8"),
  // ...
)
class MyTestClass {
  // ...
}
```

## Globale via le POM

```xml
<project>
  <!-- ... -->
  <build>
    <!-- ... -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <configuration>
        <argLine>@{argLine} -Dfile.encoding=UTF-8</argLine>
      </configuration>
    </plugin>
    <!-- ... -->
  </build>
  <!-- ... -->
</project>
```

Détail syntaxique : [late-property-evaluation](https://maven.apache.org/surefire/maven-surefire-plugin/faq.html#late-property-evaluation)

## Globale en ligne de commandes

```sh
./mvnw test -DargLine="@{argLine} -Dfile.encoding=UTF-8"
```

## Globale via un environnement Java plus récent

Ce problème ne devrait plus se produire à partir de Java 18 : cf. [JEP 400 "UTF-8 by Default"](https://openjdk.org/jeps/400)
