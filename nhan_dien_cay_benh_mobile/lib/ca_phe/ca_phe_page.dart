import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:http/http.dart' as http;

class CaPhePage extends StatefulWidget {
  const CaPhePage({super.key});

  @override
  State<CaPhePage> createState() => _CaPhePageState();
}

class _CaPhePageState extends State<CaPhePage> {
  File? _imageFile;
  final ImagePicker _picker = ImagePicker();
  String? _responseMessage;

  // Chọn ảnh từ thư viện
  Future<void> _pickImage() async {
    final pickedFile = await _picker.pickImage(source: ImageSource.gallery);
    if (pickedFile != null) {
      setState(() {
        _imageFile = File(pickedFile.path);
      });
    }
  }

  // Chụp ảnh bằng camera
  Future<void> _takePhoto() async {
    final pickedFile = await _picker.pickImage(source: ImageSource.camera);
    if (pickedFile != null) {
      setState(() {
        _imageFile = File(pickedFile.path);
      });
    }
  }

  // Gửi ảnh lên API
  Future<void> _uploadImage() async {
    if (_imageFile == null) {
      setState(() {
        _responseMessage = 'No image selected!';
      });
      return;
    }
    showDialog(
      context: context,
      barrierDismissible: false, // Không cho phép đóng dialog khi chạm ngoài
      builder: (BuildContext context) {
        return const Center(
          child: CircularProgressIndicator(), // Hiển thị loading
        );
      },
    );
    try {
      var request = http.MultipartRequest(
        'POST',
        Uri.parse('http://139.99.116.68:5000/api/la-cafe'), // Thay API Flask URL
      );
      request.files
          .add(await http.MultipartFile.fromPath('image', _imageFile!.path));

      var response = await request.send();
      var responseBody = await response.stream.bytesToString();

      if (response.statusCode == 200) {
        setState(() {
          _responseMessage = responseBody;
        });
      } else {
        setState(() {
          _responseMessage = 'Error: $responseBody';
        });
      }
    } catch (e) {
      setState(() {
        _responseMessage = 'Failed to upload image: $e';
      });
    } finally {
      Navigator.of(context).pop(); // Đóng dialog loading sau khi xong
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Lá cà phê'),
        centerTitle: true,
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          _imageFile != null
              ? Image.file(_imageFile!,
                  height: 300, width: 400, fit: BoxFit.contain)
              : const Text('Chưa có ảnh nào được chọn'),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: _pickImage,
                child: const Text('Chọn ảnh từ thư viện'),
              ),
              const SizedBox(width: 10),
              ElevatedButton(
                onPressed: _takePhoto,
                child: const Text('Chụp ảnh'),
              ),
            ],
          ),
          const SizedBox(height: 20),
          ElevatedButton(
            onPressed: _uploadImage,
            child: const Text('Dự đoán bệnh'),
          ),
          const SizedBox(height: 20),
          Expanded(
            child: SingleChildScrollView(
              child: Container(
                padding: const EdgeInsets.all(16),
                child: Text(
                  _responseMessage ?? '',
                  textAlign: TextAlign.left,
                  style: const TextStyle(
                    color: Colors.green,
                    fontSize: 16,
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
