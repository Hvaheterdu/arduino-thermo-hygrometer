import { readdirSync, statSync } from "node:fs";
import path from "node:path";

const javascriptBudgetKilobytes = Number.parseFloat(process.env.APP_MAX_BUNDLE_JS_KB ?? "500");
const cssBudgetKilobytes = Number.parseFloat(process.env.APP_MAX_BUNDLE_CSS_KB ?? "30");

const distAssetsDirectory = path.resolve(import.meta.dirname, "../dist/assets");

const fileNames = readdirSync(distAssetsDirectory);

const javascriptBundleFiles = fileNames.filter((fileName) => fileName.endsWith(".js"));
const cssBundleFiles = fileNames.filter((fileName) => fileName.endsWith(".css"));

const toKilobytes = (bytes) => {
  return bytes / 1024;
};

const largestFileSizeKilobytes = (files) => {
  if (files.length === 0) {
    return 0;
  }

  const fileSizes = files.map((fileName) => {
    const filePath = path.resolve(distAssetsDirectory, fileName);
    return toKilobytes(statSync(filePath).size);
  });

  return Math.max(...fileSizes);
};

const largestJavascriptBundleKilobytes = largestFileSizeKilobytes(javascriptBundleFiles);
const largestCssBundleKilobytes = largestFileSizeKilobytes(cssBundleFiles);

if (largestJavascriptBundleKilobytes > javascriptBudgetKilobytes) {
  throw new Error(
    `Largest JavaScript bundle is ${largestJavascriptBundleKilobytes.toFixed(2)} KB, exceeding budget of ${javascriptBudgetKilobytes.toFixed(2)} KB.`
  );
}

if (largestCssBundleKilobytes > cssBudgetKilobytes) {
  throw new Error(
    `Largest CSS bundle is ${largestCssBundleKilobytes.toFixed(2)} KB, exceeding budget of ${cssBudgetKilobytes.toFixed(2)} KB.`
  );
}
