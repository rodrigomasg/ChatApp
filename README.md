# Aplicación de Chat

- Ejemplo practico de una aplicacón de chat

<p align="center">
<a>
<img src="https://firebasestorage.googleapis.com/v0/b/chatapp-c60a2.appspot.com/o/screens_chatapp.jpg?alt=media&token=7391e8f6-52a2-45ec-a26d-43623e090a8f" style="height: 75%; width:75%;" alt=""/>
</a>
</p>

---

## Tegnologias utilizadas

El proyecto contiene:

- SplashScreend (androidx)
- Arquitectura MVVM
- CleanCode
- Fragments
- Navigation Component
- FirebaseAuth(phone)
- Firebase Firestore
- Firebase Storage
- Dagger
- ViewModel

---

## Uso

- Clona el repo
    - ```git clone https://github.com/rodrigomasg/ChatApp```
- Sincroniza 'ChatApp' a un proyecto en 'Firebase Console'
- Habilita  'Authentication(phone)', 'Cloud Firestore', 'Storage' en la consola de Firebase
- Asegurate de tener el archivo 'google-services.json' en (Android vista project):
  - ChatApp/app/google-services.json
- Al codigo de telefono para auth 'opt', este codigo esta limitado por los servicion de google 
  - Agregar telefono y codigo para pruebas, desde la consola en:
    - Firebase Console ->  Authentication -> Metodos de acceso -> Números de teléfono para la prueba (
    opcional)
- Para realizar pruebas puedes hacer un 'hardcode' en los archivos
    - .../data/AuthFirebaseService.kt/
      - ```fun loginPhone(){
        fun loginPhone(){
            //comentar o descomentar de acuerdo el uso
            firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+52 9999999922", "222222")
        }
    - .../ui/login/Login2Fragment
       - ```fun onCreate(){
         fun onCreate(){
            //debido a que el numero se obtiene de los args ponemos el mismo aqui para asegurar el codigo de validación
             txtNumber = args.number //numero intruducido
             txtNumber = "+52 9999999922" //numero existente de prueba
         }
    - .../ui/login/Login2ViewModel
      - ```fun loginPhone(){ onVerificationCompleted(){......} }
        fun loginPhone(){ 
            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    //en el callback descomentamos de acuerdo al caso
                    onVerificationCodeComplete()//de prueba para hacer la validacion
                    val res = authFirebaseService.completeRegisterCredential(p0)....// para comprobar codigos enviado y recibido
            }...
        }
    - ../data/AuthFirebaseService.kt
      - ```fun getCurrentUid()
        fun getCurrentUid(){
            //debido a que validamos si existe un usuario ponemos el 'uuid' de los numero en la lista de Auth de firebase
             return firebaseAuth.uid //registro 
             return "cfv..." //registro existente de prueba
        }
        
---
  
