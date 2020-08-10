with import <nixpkgs> {};
let
in stdenv.mkDerivation rec {
  name = "shadow-cljs-isomorphic-clj-template";
  env = buildEnv {
    name = name;
    paths = buildInputs;
  };
  buildInputs = [
    leiningen
  ];

  shellHook = ''
  '';
}
