import i18n from "i18next";
import Backend from "i18next-http-backend";
import { initReactI18next } from "react-i18next";
import { appConfiguration } from "../appConfig";

export function configureLocalisation(): void {
  i18n
    .use(Backend)
    .use(initReactI18next)
    .init({
      lng: appConfiguration.defaultLanguage,
      fallbackLng: appConfiguration.fallbackLanguage,
      supportedLngs: ["nb"],
      ns: ["translations"],
      interpolation: {
        escapeValue: true,
      },
      returnNull: false,
      backend: {
        loadPath: "/locales/{{lng}}/{{ns}}.json",
      },
    });
}
