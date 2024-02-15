import 'package:flutter/material.dart';
import 'package:mytracker_sdk/mytracker_sdk.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Инициализация MyTracker
  const sdkKey = '53362303610112658767';
  await MyTracker.setDebugMode(true);
  await MyTracker.trackerConfig.setBufferingPeriod(5);
  await MyTracker.init(sdkKey);
  await MyTracker.trackEvent(
    'TestEventiOS',
    {'name': 'testios'},
  );
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
