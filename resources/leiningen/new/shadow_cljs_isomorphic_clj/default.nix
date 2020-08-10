with import <nixpkgs> {};
let
in stdenv.mkDerivation rec {
  name = "{{name}}";
  env = buildEnv {
    name = name;
    paths = buildInputs;
  };
  buildInputs = [
    leiningen
    nodejs-10_x
  ];

  shellHook = ''
    export PATH=$PATH:$(npm bin)
    alias start-server='lein run'
    alias compile-cljs='lein shadow compile app'
    alias watch-cljs='lein shadow watch app'

    alias start-all='compile-cljs; start-server'
    cat default.nix | grep '^ \+\(function\|alias\) .\+'
  '';
}
