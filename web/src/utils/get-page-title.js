import i18n from "@/utils/i18n";
const title = i18n.t("domain.oneStopIntelligentComputingCloudSolution");

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} | ${title}`;
  }
  return `${title}`;
}
