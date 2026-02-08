SPRINT 1


Travail effectué :
- Création d’un diagramme de cas d’utilisation afin de définir
	clairement les objectifs de l’application

- Création des différentes classes. On applique le principe
	qui consiste à associer une méthode unique à une classe spécifique.

- Migration des méthodes dans les classes correspondantes créées plus
	tôt. Au lieu d’avoir toutes les méthodes dans la classe Menu et que
	le code soit complexe, chaque classe a sa méthode et la classe Menu ne
	fait que les appeler lorsque cela est nécessaire.

- Correction de la méthode addGame. On évite désormais les champs vides et
	les valeurs erronées (exemples : -2 pour le nombre de joueurs minimum,
	ou un nombre de joueurs maximum plus petit que le nombre de joueurs minimum). 




======================================================================================

SPRINT 2

Travail effectué :
- Modification de la classe AddGame afin d'éviter les doublons dans la collection
	(jeux avec le même nom).

- Modification du fichier test AddGameTest afin de vérifier le cas où l'on voudrait
	créer un jeu avec le même nom qu'un de ceux qui existent déjà dans la collection.

- Ajout du rapport en .md dans le projet.

- Modification et ajout du diagramme uml lié au projet.

- Implémentation de la feature "Week-End Summary" avec ses tests.

- Modification du fichier test RemoveGameTest en raison d'une erreur de compabilité
    de Mockito.

- Implémentation de la feature "Recommend Game" qui recommande aléatoirement un jeu
    compatible avec le nombre de joueurs spécifié par l'utilisateur.


=====================================================================================

SPRINT 3

Travail effectué :
- Ajout de la feature "Games for X players".

- Ajout du fichier test pour la feature Games for X players.

- Modification du menu pour afficher les nouvelles options.

- Ajout de la feature "Undo" qui doit seulement fonctionner pour Add ou Remove Game.

- Ajout du fichier test pour "Undo".

- Modifications des tests de Add et Remove afin de concorder avec la nouvelle feature Undo.

- Modification et fix de la feature "WeekendSummary" et de son test.
