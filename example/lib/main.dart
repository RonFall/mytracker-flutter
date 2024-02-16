import 'package:flutter/material.dart';
import 'package:mytracker_sdk/mytracker_sdk.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Инициализация MyTracker
  const sdkKey = 'api_key';
  await MyTracker.setDebugMode(true);
  await MyTracker.init(sdkKey);

  await testConfig();

  runApp(const MyApp());
}

Future<void> testConfig() async {
  await MyTracker.trackRegistrationEvent(
    'trackRegistrationEvent',
    {'type': 'trackRegistrationEvent'},
  );
  await MyTracker.trackEvent(
    'trackEvent',
    {'type': 'trackEvent'},
  );
  await MyTracker.trackLoginEvent(
    'trackLoginEvent',
    {'type': 'trackLoginEvent'},
  );
  await MyTracker.flush();
  await MyTracker.isDebugMode();

  final params = MyTracker.trackerParams;
  await params.setCustomUserIds(['customUserIds']);
  await params.setGender(MyTrackerGender.FEMALE);
  await params.setAge(22);
  await params.setEmails(['email']);
  await params.setLang('EN');
  await params.setPhones(['4455']);

  await params.getCustomUserIds();
  await params.getGender();
  await params.getEmails();
  await params.getLang();
  await params.getPhones();

  final config = MyTracker.trackerConfig;
  await config.setBufferingPeriod(55);
  await config.setTrackingEnvironmentEnabled(true);
  await config.setForcingPeriod(73);
  await config.setLaunchTimeout(234);
  await config.setProxyHost('host');
  await config.setRegion(MyTrackerRegion.EU);
  await config.setTrackingLaunchEnabled(false);
  await config.setTrackingLocationEnabled(true);

  await config.getBufferingPeriod();
  await config.getForcingPeriod();
  await config.getId();
  await config.getLaunchTimeout();
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MyTracker Demo',
      home: Scaffold(
        appBar: AppBar(title: const Text('MyTracker Demo')),
        body: SafeArea(
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                ElevatedButton(
                  onPressed: () {
                    MyTracker.trackEvent(
                      'TestEventiOS',
                      {'name': 'testios'},
                    );
                  },
                  child: const Text('Send event'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
