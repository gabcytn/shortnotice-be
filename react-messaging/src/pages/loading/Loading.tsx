import styles from "./Loading.module.css";
type LoadingProps = {
  visible: boolean;
};
function Loading({ visible }: LoadingProps) {
  if (!visible) return null;
  return (
    <div className={styles.wrapper}>
      <div className={styles.loader}></div>
      <div className={styles.loadingText}>Loading...</div>
    </div>
  );
}

export default Loading;
