const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendFriendRequestNotification = functions.firestore
    .document("friendRequests/{receiverUsername}/{senderUsername}")
    .onCreate(async (snap, context) => {
      const {receiverUsername, senderUsername} = context.params;

      try {
        const userDoc = await admin.firestore()
            .collection("users")
            .doc(receiverUsername)
            .get();

        if (!userDoc.exists) {
          console.log(`User ${receiverUsername} not found`);
          return null;
        }

        const fcmToken = userDoc.data().fcmToken;

        if (!fcmToken) {
          console.log(`No FCM token for user ${receiverUsername}`);
          return null;
        }

        const message = {
          notification: {
            title: "Nuova richiesta di amicizia",
            body: `${senderUsername} ti ha inviato una richiesta`,
          },
          token: fcmToken,
          data: {
            type: "friend_request",
            fromUser: senderUsername,
          },
        };

        const response = await admin.messaging().send(message);
        console.log("Notifica inviata:", response);
        return response;
      } catch (error) {
        console.error("Errore nell'invio della notifica:", error);
        return null;
      }
    });
