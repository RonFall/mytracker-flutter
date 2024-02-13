import 'package:flutter/material.dart';
import 'package:mytracker_sdk/mytracker_sdk.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Инициализация MyTracker
  const sdkKey = '';
  await MyTracker.init(sdkKey);

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MyTracker Demo',
      home: Scaffold(
        appBar: AppBar(title: const Text('MyTracker Demo')),
        body: const SafeArea(
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text('MyTracker'),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
