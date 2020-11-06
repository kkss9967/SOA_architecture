rmiregistry=($(lsof -i :1099))
BASEDIR=$(dirname "$0")
if [ "${rmiregistry[11]}" ]; then
  kill -9 "${rmiregistry[11]}"
  echo "kill rmiregistry and restart"
else
  echo "start rmiregistry"
fi

if [ "${PWD}" != "${BASEDIR}" ]; then
  echo "change directory to ${BASEDIR}"
  cd "${BASEDIR}" || exit
fi

rmiregistry &
