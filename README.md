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
  ```git clone https://github.com/rodrigomasg/ChatApp```
- Sincroniza 'ChatApp' a un proyecto en Firebase Console
- Habilita Auth, Firestore, Storage en la consola de Firebase
- Asegurate de tener el archivo 'google-services.json' en (Android vista project):
  ChatApp/app/google-services.json
- Al user 'opt' (token) para 'Auth', este codigo esta limitado por los servicion de google (agregar
  telefono y codigo para pruebas), desde la consola en:
  Firebase Console ->  Authentication -> Metodos de acceso -> Números de teléfono para la prueba (
  opcional)
- Puedes hacer un 'hardcode' en los archivos
    - .../data/AuthFirebaseService.kt/
      ```fun loginPhone(){//agregar numero y codigo de pruba(descomentando la linea)}```
    - .../ui/login/Login2Fragment
      ```fun onCreate(){ //txtNumber = "poner numero de pruaba(descomentando la linea)" }```
    - .../ui/login/Login2ViewModel
      - fun loginPhone(){ onVerificationCompleted(){......} }
        ```fun loginPhone(){ //en el callback descomentamos de acuerdo al caso" }```
    - ../data/AuthFirebaseService.kt
      ```fun getCurrentUid(){ //debido a que validamos si existe un usuario ponemos el 'uuid' de los numero en la lista de Auth de firebase " }```
  
