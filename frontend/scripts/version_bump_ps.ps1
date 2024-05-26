# Change to the project root directory
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptPath

while (-not (Test-Path "package.json")) {
    if ($PWD.Path -eq "\\") {
        Write-Error "Error: package.json not found in the directory hierarchy."
        exit 1
    }
    Set-Location ..
}

# Check if a version bump type argument is provided
if (-not $args) {
  Write-Host "Usage: $MyInvocation.MyCommand {patch|minor|major}"
  exit 1
}

$BUMP_TYPE = $args[0]

Write-Host "Automatically performing a $BUMP_TYPE update to project."
Write-Host

# Check if the provided argument is valid
if ($BUMP_TYPE -ne "patch" -and $BUMP_TYPE -ne "minor" -and $BUMP_TYPE -ne "major") {
  Write-Host "Invalid version type. Use 'patch', 'minor', or 'major'."
  exit 1
}

# Get the current version from package.json
$CURRENT_VERSION = Get-Content package.json | ConvertFrom-Json | Select-Object -ExpandProperty version

Write-Host "Current project version: $CURRENT_VERSION"

# Bump the version
Write-Host "New project version: $(npm version $BUMP_TYPE --no-git-tag-version)"
Write-Host

# Get the new version from package.json
$NEW_VERSION = Get-Content package.json | ConvertFrom-Json | Select-Object -ExpandProperty version

# Stage the package.json and package-lock.json files
git add .

# Commit the changes
git commit -m "Bump version from $CURRENT_VERSION to $NEW_VERSION"

Write-Host
Write-Host "Version bumped from $CURRENT_VERSION to $NEW_VERSION and changes committed successfully."
