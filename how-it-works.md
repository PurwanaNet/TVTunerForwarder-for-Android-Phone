# Berikut adalah dua bagian utama yang diperlukan untuk proyek ini:

Streaming Server (Repeater) untuk TV Digital

Mengambil data dari tuner TV digital (menggunakan Android Tuner HAL dari DTVKit).
Menggunakan FFmpeg atau MediaCodec untuk encoding.
Menggunakan UDP multicast untuk menyebarkan stream ke jaringan lokal.
Android Client (UI untuk Navigasi Channel dan Pemutaran Streaming)

Menampilkan daftar channel yang tersedia.
Memutar channel yang dipilih menggunakan ExoPlayer.
Saya akan membuatkan dua source code:

Server (Repeater) di Smart TV / Set-Top Box
Client (Aplikasi Android untuk Navigasi & Pemutaran)

Cara Kerja Keseluruhan:
Server (Repeater) menangkap data dari tuner TV digital dan menyiarkannya ke jaringan menggunakan UDP multicast.
Client (Android App) menerima daftar channel dan memungkinkan pengguna memilih serta menonton channel melalui jaringan lokal.

---

# Here are the two main parts required for this project:

Streaming Server (Repeater) for Digital TV

Fetches data from digital TV tuner (using Android Tuner HAL from DTVKit). Uses FFmpeg or MediaCodec for encoding. Uses UDP multicast to broadcast the stream to the local network. Android Client (UI for Channel Navigation and Streaming Playback)

Displays the list of available channels. Plays the selected channel using ExoPlayer. I will create two source codes:

Server (Repeater) on Smart TV / Set-Top Box Client (Android App for Navigation & Playback)

How it All Works: Server (Repeater) captures data from digital TV tuner and broadcasts it to the network using UDP multicast. Client (Android App) receives the list of channels and allows the user to select and watch channels over the local network.
