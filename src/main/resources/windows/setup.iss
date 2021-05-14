#define MyAppName "${project.artifactId}"
#define MyAppVersion "${project.version}"
#define MyAppPublisher "${distributor}"
#define MyAppURL "${homepage}"
#define MyAppExeName "${project.artifactId}.exe"
#define MyAppBasePath "${project.basedir}"

[Setup]
AppId={{9055A6D3-0402-4FFF-8588-7B61CB870759}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DisableProgramGroupPage=yes
PrivilegesRequiredOverridesAllowed=dialog
OutputDir={#MyAppBasePath}\output
OutputBaseFilename={#MyAppName}Setup
SetupIconFile={#MyAppBasePath}\src\main\resources\Images\logo.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "{#MyAppBasePath}\target\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#MyAppBasePath}\target\java\*"; DestDir: "{app}\java"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

