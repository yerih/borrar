from docx import Document
from docx.shared import Inches, Pt, Cm, RGBColor
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.text import WD_ALIGN_PARAGRAPH
import datetime

doc = Document()

style = doc.styles['Normal']
font = style.font
font.name = 'Calibri'
font.size = Pt(10)

for i in range(1, 4):
    hs = doc.styles[f'Heading {i}']
    hs.font.color.rgb = RGBColor(0, 51, 102)

doc.add_heading('MiVuelto - Reporte de Permisos y Consumo de Recursos Android', 0)
doc.add_paragraph(f'Fecha de generacion: {datetime.datetime.now().strftime("%Y-%m-%d %H:%M")}')
doc.add_paragraph('Proyecto: MiVuelto (com.mivuelto) v1.1.3')
doc.add_paragraph('')

def add_table(doc, headers, rows):
    table = doc.add_table(rows=1 + len(rows), cols=len(headers))
    table.style = 'Light Grid Accent 1'
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    hdr = table.rows[0].cells
    for i, h in enumerate(headers):
        hdr[i].text = h
        for p in hdr[i].paragraphs:
            for r in p.runs:
                r.bold = True
                r.font.size = Pt(9)
    for ri, row in enumerate(rows):
        cells = table.rows[ri + 1].cells
        for ci, val in enumerate(row):
            cells[ci].text = str(val)
            for p in cells[ci].paragraphs:
                for r in p.runs:
                    r.font.size = Pt(9)
    return table

# ============ 1. PERMISOS ANDROID ============
doc.add_heading('1. Permisos Android Declarados', level=1)
doc.add_paragraph('Permisos declarados en app/src/main/AndroidManifest.xml:')
add_table(doc,
    ['Permiso', 'Constante Android', 'Proposito', 'Nivel de Riesgo'],
    [
        ['READ_PHONE_STATE', 'android.permission.READ_PHONE_STATE', 'Lectura de estado del dispositivo (IMEI, numero de serie, numero de telefono)', 'ALTO'],
        ['READ_BASIC_PHONE_STATE', 'android.permission.READ_BASIC_PHONE_STATE', 'Subconjunto del anterior para Android 12+ (informacion basica del dispositivo)', 'MEDIO'],
    ])
doc.add_paragraph('')
doc.add_paragraph('Permisos Criticos Faltantes:')
add_table(doc,
    ['Permiso', 'Constante Android', 'Estado', 'Impacto'],
    [
        ['INTERNET', 'android.permission.INTERNET', 'FALTANTE', 'CRITICO - La app usa Retrofit/OkHttp pero no declara este permiso'],
        ['ACCESS_NETWORK_STATE', 'android.permission.ACCESS_NETWORK_STATE', 'FALTANTE', 'No puede verificar disponibilidad de red'],
        ['ACCESS_WIFI_STATE', 'android.permission.ACCESS_WIFI_STATE', 'FALTANTE', 'No puede verificar estado del WiFi'],
    ])

# ============ 2. ACCESO AL SISTEMA OPERATIVO ============
doc.add_heading('2. Analisis de Uso del Sistema Operativo', level=1)
add_table(doc,
    ['Caracteristica', 'Estado', 'Detalles'],
    [
        ['Llamadas de red (Network)', 'ACTIVO', 'Retrofit 2.9.0 + OkHttp 4.11.0, 5 endpoints de API'],
        ['Estado del telefono (Serial)', 'ACTIVO', 'Lee numero de serie desde MoreFun SDK (no desde TelephonyManager de Android)'],
        ['Binding a servicio en background', 'ACTIVO', 'Se conecta al servicio del SDK MoreFun (com.morefun.ysdk.service) via AIDL'],
        ['Camara', 'NO USADO', '--'],
        ['GPS / Ubicacion', 'NO USADO', '--'],
        ['Bluetooth', 'NO USADO', '--'],
        ['NFC', 'NO USADO', 'Solo recurso de texto (core-ui/strings.xml:137), sin codigo'],
        ['Sensores (Sensors)', 'NO USADO', '--'],
        ['Room Database', 'DECLARADO', 'Dependencias presentes, sin implementar @Database/@Entity/@Dao'],
        ['SharedPreferences', 'NO USADO', '--'],
        ['DataStore', 'NO USADO', '--'],
        ['WebView', 'NO USADO', '--'],
        ['Carga de imagenes (Glide/Coil)', 'NO USADO', '--'],
        ['MediaPlayer / ExoPlayer', 'NO USADO', '--'],
        ['WorkManager', 'NO USADO', '--'],
        ['AlarmManager / JobScheduler', 'NO USADO', '--'],
        ['ContentProvider', 'NO USADO', '--'],
        ['BroadcastReceiver', 'NO USADO', '--'],
    ])

# ============ 3. ENDPOINTS DE API ============
doc.add_heading('3. Endpoints de API (5 en total)', level=1)
add_table(doc,
    ['Endpoint', 'Metodo', 'Requiere Auth', 'Referencia'],
    [
        ['app/login', 'POST', 'No', 'core-data/.../ApiService.kt:18'],
        ['banks', 'GET', 'Bearer token', 'core-data/.../ApiService.kt:21'],
        ['configs', 'GET', 'Bearer token', 'core-data/.../ApiService.kt:24'],
        ['transactions/query', 'POST', 'Bearer token', 'core-data/.../ApiService.kt:30'],
        ['router/movimientos', 'GET', 'Bearer token', 'core-data/.../ApiService.kt:36'],
    ])

# ============ 4. SDK EXTERNO ============
doc.add_heading('4. SDK Externo - Terminal de Pagos MoreFun', level=1)
add_table(doc,
    ['Propiedad', 'Valor'],
    [
        ['Archivo del SDK', 'ysdk_5.91.c221d74_24092716.jar'],
        ['Version del SDK', '5.91'],
        ['Nombre del servicio', 'com.morefun.ysdk.service'],
        ['Tipo de binding', 'AIDL con BIND_AUTO_CREATE'],
        ['Uso', 'Lee el numero de serie del dispositivo al iniciar la app'],
        ['Archivos fuente', 'ConnectionDeviceEngine.kt, MainApplication.kt, SdkModuleDI.kt, EngineRequester.kt'],
    ])

# ============ 5. CONSUMO DE RECURSOS ============
doc.add_heading('5. Estimacion de Consumo de Recursos', level=1)

doc.add_heading('5.1 Memoria (RAM) - Desglose por Modulo', level=2)
add_table(doc,
    ['Componente', 'RAM Estimada', 'Notas'],
    [
        ['App Base (Core)', '15-25 MB', 'Compose runtime, Navigation, Hilt DI, Coroutines'],
        ['Core-Data', '8-15 MB', 'Connection pool de Retrofit + OkHttp, Gson'],
        ['Core-UI', '5-10 MB', 'Componentes UI de Compose, Material3'],
        ['Feature-Purchase', '5-8 MB', 'Flujo de login, pantallas Compose'],
        ['Feature-Home', '5-8 MB', 'Dashboard principal, pantallas Compose'],
        ['Feature-Send-Change', '4-7 MB', 'Flujo de envio de cambio'],
        ['Feature-Instant-Debit', '4-7 MB', 'Flujo de debito instantaneo'],
        ['SDK MoreFun (AIDL)', '10-20 MB', 'SDK externo de terminal de pagos, bindings nativos'],
        ['Animaciones Lottie', '3-5 MB', 'Cuando hay animaciones activas'],
        ['TOTAL ESTIMADO (Idle)', '55-95 MB', 'En tiempo de ejecucion, estado inactivo'],
        ['TOTAL PICO', '120-180 MB', 'Durante operaciones activas (red + animaciones + SDK)'],
    ])

doc.add_heading('5.2 Uso de CPU', level=2)
add_table(doc,
    ['Componente', 'Impacto CPU', 'Detalles'],
    [
        ['Renderizado de Compose', 'Medio-Alto', 'Recomposicion de UI, calculos de layout'],
        ['OkHttp (Red)', 'Bajo-Medio', 'Parsing JSON (Gson), manejo HTTP'],
        ['SDK MoreFun', 'Medio', 'IPC via AIDL, comunicacion con terminal de pago'],
        ['Hilt DI', 'Bajo', 'Inyeccion de dependencias solo al inicio'],
        ['Coroutines', 'Bajo', 'Operaciones asincronas (red, DB)'],
        ['Animaciones Lottie', 'Medio', 'Al reproducir (splash screen, transiciones)'],
        ['Room', 'Bajo', 'Aun no implementado'],
    ])

doc.add_heading('5.3 Tamano del APK (Estimado)', level=2)
add_table(doc,
    ['Componente', 'Tamano Estimado'],
    [
        ['Compose UI + Material3', '8-12 MB'],
        ['SDK MoreFun (JAR)', '5-10 MB'],
        ['Retrofit + OkHttp + Gson', '1-2 MB'],
        ['Hilt + KSP generado', '2-4 MB'],
        ['Lottie', '1-2 MB'],
        ['Room (sin usar)', '1-2 MB'],
        ['Navigation Compose', '1-2 MB'],
        ['TOTAL APK', '~20-35 MB'],
    ])

# ============ 6. PROBLEMAS DE SEGURIDAD ============
doc.add_heading('6. Problemas de Seguridad Identificados', level=1)
add_table(doc,
    ['Problema', 'Severidad', 'Ubicacion'],
    [
        ['Permiso INTERNET faltante', 'CRITICO', 'AndroidManifest.xml'],
        ['Sin network_security_config.xml', 'ALTO', 'Archivo inexistente'],
        ['Sin certificate pinning', 'ALTO', 'NetworkModule.kt'],
        ['URL base de API con placeholder (api.example.com)', 'MEDIO', 'NetworkModule.kt:35'],
        ['Sin almacenamiento encriptado', 'MEDIO', 'Sin SharedPreferences/DataStore implementado'],
        ['allowBackup="true"', 'MEDIO', 'AndroidManifest.xml:9'],
        ['Sin reglas ProGuard/R8', 'BAJO', 'proguard-rules.pro inexistente'],
        ['Logging Level.BODY de OkHttp en release', 'BAJO', 'NetworkModule.kt:26'],
        ['Credenciales de signing con placeholder', 'BAJO', 'app/build.gradle.kts'],
    ])

# ============ 7. CONFIGURACION DEL PROYECTO ============
doc.add_heading('7. Resumen de Configuracion del Proyecto', level=1)
add_table(doc,
    ['Propiedad', 'Valor'],
    [
        ['Application ID', 'com.mivuelto'],
        ['Version Name', '1.1.3'],
        ['Version Code', '5'],
        ['minSdk', '26 (Android 8.0 Oreo)'],
        ['targetSdk', '34 (Android 14)'],
        ['compileSdk', '34 (app/core) / 35 (features)'],
        ['Kotlin', '1.9.22'],
        ['Gradle', '9.0.0'],
        ['Android Gradle Plugin (AGP)', '8.5.0'],
        ['Java Target', '21'],
        ['Compose BOM', '2023.08.00'],
        ['R8/ProGuard', 'DESACTIVADO (isMinifyEnabled = false)'],
        ['Gradle JVM Heap', '4096 MB (-Xmx4096m)'],
    ])

doc.add_heading('Grafico de Dependencias entre Modulos', level=2)
p = doc.add_paragraph()
p.style = 'No Spacing'
p.add_run(
    'app\n'
    '  +-- core\n'
    '  +-- core-data --> core\n'
    '  +-- core-ui --> core\n'
    '  +-- feature-check-payment --> core, core-ui\n'
    '  +-- feature-home --> core, core-ui\n'
    '  +-- feature-send-change --> core, core-ui\n'
    '  +-- feature-instant-debit --> core, core-ui'
)

# ============ 8. RECOMENDACIONES ============
doc.add_heading('8. Recomendaciones', level=1)
recs = [
    ('Agregar permiso INTERNET al AndroidManifest.xml', 'CRITICO'),
    ('Crear network_security_config.xml con certificate pinning', 'ALTO'),
    ('Activar R8/ProGuard (isMinifyEnabled = true) en builds de release', 'ALTO'),
    ('Reemplazar la URL placeholder de la API (api.example.com) por el endpoint de produccion', 'ALTO'),
    ('Implementar EncryptedSharedPreferences para almacenar tokens y sesiones', 'MEDIO'),
    ('Configurar allowBackup="false" o definir reglas de data extraction', 'MEDIO'),
    ('Remover el logging HTTP body en builds de release', 'MEDIO'),
    ('Unificar compileSdk en todos los modulos (34 vs 35)', 'MEDIO'),
    ('Agregar configuracion de timeout al OkHttpClient', 'BAJO'),
    ('Evaluar si READ_PHONE_STATE es realmente necesario (el serial viene del MoreFun SDK)', 'BAJO'),
]
for i, (rec, sev) in enumerate(recs, 1):
    doc.add_paragraph(f'{i}. [{sev}] {rec}')

# ============ 9. LISTA COMPLETA DE DEPENDENCIAS ============
doc.add_heading('9. Lista Completa de Dependencias', level=1)
add_table(doc,
    ['Libreria', 'Version', 'Modulo'],
    [
        ['AndroidX Core KTX', '1.10.1', 'core'],
        ['AndroidX Activity Compose', '1.7.2', 'core'],
        ['Compose BOM', '2023.08.00', 'core, core-ui, features'],
        ['Compose UI', '(BOM)', 'core, core-ui, features'],
        ['Compose Material3', '(BOM)', 'core, core-ui, features'],
        ['Compose ConstraintLayout', '1.1.1', 'core, features'],
        ['Navigation Compose', '2.7.1', 'core, features'],
        ['Hilt Android', '2.48', 'core, core-data, features, app'],
        ['Hilt Compiler (KSP)', '2.48', 'core, core-data, features, app'],
        ['Hilt Navigation Compose', '1.0.0', 'core, features'],
        ['Lifecycle Runtime KTX', '2.6.1', 'core'],
        ['Lifecycle ViewModel KTX', '2.6.1', 'core'],
        ['Lifecycle ViewModel Compose', '2.6.1', 'core'],
        ['Retrofit', '2.9.0', 'core, core-data'],
        ['Retrofit Gson Converter', '2.9.0', 'core, core-data'],
        ['OkHttp', '4.11.0', 'core-data'],
        ['OkHttp Logging Interceptor', '4.11.0', 'core-data'],
        ['OkHttp Profiler', '1.0.8', 'core-data'],
        ['Room Runtime', '2.5.2', 'core, core-data'],
        ['Room KTX', '2.5.2', 'core, core-data'],
        ['Room Compiler (KSP)', '2.5.2', 'core-data'],
        ['Lottie Compose', '6.7.1', 'core'],
        ['KotlinX Coroutines', '1.7.3', 'core'],
        ['MoreFun SDK (ysdk)', '5.91', 'app (JAR local)'],
    ])

# ============ GUARDAR ============
output_path = r'C:\trabajo\repositorios\mi-vuelto\MiVuelto_Reporte_Permisos_Recursos.docx'
doc.save(output_path)
print(f'Reporte guardado en: {output_path}')
