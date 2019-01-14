# SecureChat
SecureChat est une application de messagerie en ligne en Android natif développée entièrement en Java, ses données sont stockées sur Firebase.

Dans un premier temps, un utilisateur peut se connecter ou créer un compte pour accéder aux écrans principaux de l'application, cette gestion des utilisateurs est également gérée par Firebase et la connexion se fait par simple couple email/mot de passe, alors qu'à la création du compte un nom d'utilisateur est également requis.
Un utilisateur qui se serait connecté à l'application sans s'y déconnecter se vera reconnecter automatiquement à sa prochaine visite.

Deux collections de documents sont utilisées pour en assurer le fonctionnement : 
- La première correspond aux informations basiques des utilisateurs liées à leurs comptes (nom d'utilisateur, email et UID), ainsi qu'une liste de contacts pour chacun de ces utilisateurs à qui ils peuvent communiquer. Ces informations sont utilsées pour s'assurer de la cohérence des données stockées localement et non pas pour la connexion. 
- Le seconde correspond aux chats entre deux utilisateurs. L'ID de ces documents est composé des UID de chacun des utilisateurs et contient la liste de tous les messages envoyées (corps du message, ID de l'utilisateur ayant envoyé le message et un timestamp pour s'assurer de la chronologie)

Une fois connecté à l'application, l'utilisateur sera présenté à sa liste de contacts. Ici, plusieurs actions sont possibles :
- Ajouter un nouveau contact en cliquant sur le floating action button en bas à droite de l'écran. L'utilisateur sera ensuite redirigé vers un écran où il pourra ajouter un nouvel utilisateur à ses contacts graçe à son nom d'utilisateur ou son adresse mail
- En mode portrait, en cliquant sur un des contacts, afficher le chat avec l'utilisateur sélectionné
- En mode paysage, afficher côte-à-côte la liste des contacts et ses messages associées
- Se déconnecter, en appuyant sur le bouton dans la barre de titre

La récupération des nouveaux messages et contacts se fait automatiquement, même si l'application n'est pas ouverte : A la réception d'un nouveau message une notification est envoyé et une fois cliquée elle ouvre le chat concerné.
Au lancement de l'application cette procédure de vérification de réception se fait toutes les 5 secondes jusqu'à atteindre 30 secondes sans avoir reçu de messages, auquel cas la vérification se fera toutes les 30 secondes. A partir du moment où un message est reçu, ce compte à rebours recommence et la vérification s'effectue à nouveau toutes les 5 secondes.

Graçe à l'intent SEND, l'utilisateur peut partager n'importe quel contenu textuel d'une autre application afin de l'envoyer à un de ses contacts.


---


Installation
-

- Assurez-vous d'avoir d'abord connecté un appareil à votre ordinateur ou d'avoir préparé un émulateur
- Installez ensuite l'APK du projet depuis Android Studio 
- L'application devrait se lancer toute seule, si ce n'est pas le cas vous la trouverez dans vos applications

---

Projet M2 MBDS Nice Sophia Antipolis - Promo 2018/2019 - Enseignant: Alexandre Maisonobe - Langage : Java
